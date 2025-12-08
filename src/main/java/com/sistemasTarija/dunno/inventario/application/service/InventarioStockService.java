package com.sistemasTarija.dunno.inventario.application.service;

import com.sistemasTarija.dunno.inventario.application.dto.*;
import com.sistemasTarija.dunno.inventario.application.port.in.ConsultarInventarioUseCase;
import com.sistemasTarija.dunno.inventario.application.port.out.InventarioPersistencePort;
import com.sistemasTarija.dunno.inventario.domain.exception.InventarioNotFoundException;
import com.sistemasTarija.dunno.inventario.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioStockService implements ConsultarInventarioUseCase {
    
    private final InventarioPersistencePort persistencePort;
    
    @Override
    public List<InventarioResumenDTO> obtenerListadoGeneral() {
        // Utiliza query optimizada que hace SUM directamente en BD
        return persistencePort.obtenerResumenGlobal();
    }
    
    @Override
    public InventarioDetalleDTO obtenerDetalleModelo(Integer idModelo) {
        // 1. Buscar el modelo con todos sus datos
        Modelo modelo = persistencePort.findModeloById(idModelo)
                .orElseThrow(() -> new InventarioNotFoundException(
                        "El modelo con ID " + idModelo + " no existe"));
        
        // 2. Obtener todos los registros de inventario del modelo
        List<Inventario> inventarios = persistencePort.findInventariosByModelo(idModelo);
        
        // 3. Obtener todas las sucursales activas
        List<Sucursal> sucursales = persistencePort.findAllSucursales();
        
        // 4. Construir el DTO de respuesta
        InventarioDetalleDTO detalleDTO = new InventarioDetalleDTO();
        detalleDTO.setIdModelo(modelo.getId());
        
        // 5. Extraer colores y tallas disponibles del modelo
        Set<String> coloresSet = new HashSet<>();
        Set<String> tallasSet = new HashSet<>();
        
        // Mapa para acceso rápido: idVariante -> {color, talla}
        Map<Integer, VarianteInfo> varianteInfoMap = new HashMap<>();
        
        for (ModeloColor modeloColor : modelo.getColores()) {
            String nombreColor = modeloColor.getColor() != null ? modeloColor.getColor().getNombre() : "";
            String codigoHex = modeloColor.getColor() != null ? modeloColor.getColor().getCodigoHex() : "";
            coloresSet.add(nombreColor);
            
            for (Variante variante : modeloColor.getVariantes()) {
                String nombreTalla = variante.getTalla() != null ? variante.getTalla().getNombre() : "";
                tallasSet.add(nombreTalla);
                
                varianteInfoMap.put(variante.getId(), new VarianteInfo(
                        variante.getId(), nombreColor, codigoHex, nombreTalla
                ));
            }
        }
        
        detalleDTO.setColoresDisponibles(new ArrayList<>(coloresSet));
        detalleDTO.setTallasDisponibles(new ArrayList<>(tallasSet));
        
        // 6. Agrupar inventarios por sucursal
        Map<Integer, List<Inventario>> inventariosPorSucursal = inventarios.stream()
                .collect(Collectors.groupingBy(Inventario::getIdSucursal));
        
        // 7. Crear DTO para cada sucursal
        List<SucursalStockDTO> sucursalesDTOs = new ArrayList<>();
        
        for (Sucursal sucursal : sucursales) {
            SucursalStockDTO sucursalDTO = construirSucursalDTO(
                    sucursal, 
                    inventariosPorSucursal.get(sucursal.getId()),
                    varianteInfoMap,
                    coloresSet,
                    tallasSet
            );
            sucursalesDTOs.add(sucursalDTO);
        }
        
        // 8. Crear sucursal "Global" sumando todos los stocks
        SucursalStockDTO globalDTO = construirSucursalGlobal(
                inventarios,
                varianteInfoMap,
                coloresSet,
                tallasSet
        );
        sucursalesDTOs.add(globalDTO);
        
        detalleDTO.setSucursales(sucursalesDTOs);
        
        return detalleDTO;
    }
    
    /**
     * Construye el DTO de una sucursal con su matriz Color x Talla
     */
    private SucursalStockDTO construirSucursalDTO(
            Sucursal sucursal,
            List<Inventario> inventarios,
            Map<Integer, VarianteInfo> varianteInfoMap,
            Set<String> colores,
            Set<String> tallas) {
        
        SucursalStockDTO dto = new SucursalStockDTO();
        dto.setIdSucursal(sucursal.getId());
        dto.setNombreSucursal(sucursal.getNombre());
        
        // Crear la matriz Color x Talla
        Map<String, Map<String, VarianteStockDTO>> matriz = new LinkedHashMap<>();
        
        // Inicializar matriz con stocks en 0
        for (String color : colores) {
            Map<String, VarianteStockDTO> tallasMap = new LinkedHashMap<>();
            for (String talla : tallas) {
                tallasMap.put(talla, null); // Placeholder
            }
            matriz.put(color, tallasMap);
        }
        
        // Llenar con datos reales del inventario
        if (inventarios != null) {
            for (Inventario inv : inventarios) {
                VarianteInfo info = varianteInfoMap.get(inv.getIdVariante());
                if (info != null) {
                    VarianteStockDTO varianteDTO = VarianteStockDTO.builder()
                            .idVariante(info.idVariante)
                            .nombreColor(info.nombreColor)
                            .codigoHexColor(info.codigoHex)
                            .nombreTalla(info.nombreTalla)
                            .stock(inv.getStock() != null ? inv.getStock() : 0)
                            .build();
                    
                    Map<String, VarianteStockDTO> tallasMap = matriz.get(info.nombreColor);
                    if (tallasMap != null) {
                        tallasMap.put(info.nombreTalla, varianteDTO);
                    }
                }
            }
        }
        
        // Reemplazar nulls con stocks en 0
        for (Map.Entry<String, Map<String, VarianteStockDTO>> colorEntry : matriz.entrySet()) {
            String color = colorEntry.getKey();
            Map<String, VarianteStockDTO> tallasMap = colorEntry.getValue();
            
            for (Map.Entry<String, VarianteStockDTO> tallaEntry : tallasMap.entrySet()) {
                if (tallaEntry.getValue() == null) {
                    String talla = tallaEntry.getKey();
                    
                    // Buscar el idVariante correspondiente
                    Integer idVariante = varianteInfoMap.entrySet().stream()
                            .filter(e -> e.getValue().nombreColor.equals(color) 
                                      && e.getValue().nombreTalla.equals(talla))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null);
                    
                    String codigoHex = varianteInfoMap.values().stream()
                            .filter(v -> v.nombreColor.equals(color))
                            .map(v -> v.codigoHex)
                            .findFirst()
                            .orElse("");
                    
                    VarianteStockDTO zeroStock = VarianteStockDTO.builder()
                            .idVariante(idVariante)
                            .nombreColor(color)
                            .codigoHexColor(codigoHex)
                            .nombreTalla(talla)
                            .stock(0)
                            .build();
                    
                    tallaEntry.setValue(zeroStock);
                }
            }
        }
        
        dto.setMatrizColorTalla(matriz);
        return dto;
    }
    
    /**
     * Construye el DTO "Global" sumando stocks de todas las sucursales
     */
    private SucursalStockDTO construirSucursalGlobal(
            List<Inventario> todosInventarios,
            Map<Integer, VarianteInfo> varianteInfoMap,
            Set<String> colores,
            Set<String> tallas) {
        
        SucursalStockDTO dto = new SucursalStockDTO();
        dto.setIdSucursal(0); // ID especial para "Global"
        dto.setNombreSucursal("Global");
        
        // Agrupar por idVariante y sumar stocks
        Map<Integer, Integer> stocksPorVariante = todosInventarios.stream()
                .collect(Collectors.groupingBy(
                        Inventario::getIdVariante,
                        Collectors.summingInt(inv -> inv.getStock() != null ? inv.getStock() : 0)
                ));
        
        // Crear la matriz
        Map<String, Map<String, VarianteStockDTO>> matriz = new LinkedHashMap<>();
        
        for (String color : colores) {
            Map<String, VarianteStockDTO> tallasMap = new LinkedHashMap<>();
            
            for (String talla : tallas) {
                // Buscar el idVariante correspondiente
                Integer idVariante = varianteInfoMap.entrySet().stream()
                        .filter(e -> e.getValue().nombreColor.equals(color) 
                                  && e.getValue().nombreTalla.equals(talla))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
                
                Integer stockTotal = 0;
                if (idVariante != null && stocksPorVariante.containsKey(idVariante)) {
                    stockTotal = stocksPorVariante.get(idVariante);
                }
                
                String codigoHex = varianteInfoMap.values().stream()
                        .filter(v -> v.nombreColor.equals(color))
                        .map(v -> v.codigoHex)
                        .findFirst()
                        .orElse("");
                
                VarianteStockDTO varianteDTO = VarianteStockDTO.builder()
                        .idVariante(idVariante)
                        .nombreColor(color)
                        .codigoHexColor(codigoHex)
                        .nombreTalla(talla)
                        .stock(stockTotal)
                        .build();
                
                tallasMap.put(talla, varianteDTO);
            }
            
            matriz.put(color, tallasMap);
        }
        
        dto.setMatrizColorTalla(matriz);
        return dto;
    }
    
    /**
     * Clase auxiliar para almacenar información de variante
     */
    private static class VarianteInfo {
        Integer idVariante;
        String nombreColor;
        String codigoHex;
        String nombreTalla;
        
        VarianteInfo(Integer idVariante, String nombreColor, String codigoHex, String nombreTalla) {
            this.idVariante = idVariante;
            this.nombreColor = nombreColor;
            this.codigoHex = codigoHex;
            this.nombreTalla = nombreTalla;
        }
    }
}
