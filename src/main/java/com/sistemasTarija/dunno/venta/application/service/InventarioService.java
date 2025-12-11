package com.sistemasTarija.dunno.venta.application.service;

import com.sistemasTarija.dunno.venta.application.dto.catalogo.ColorDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.DetallePrendaDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.TallaDTO;
import com.sistemasTarija.dunno.venta.application.port.in.FindCatalogoUseCase;
import com.sistemasTarija.dunno.venta.application.port.out.InventarioCatalogoVentaPersistancePort;
import com.sistemasTarija.dunno.venta.application.port.out.InventarioPersistancePort;
import com.sistemasTarija.dunno.venta.domain.exception.InventarioFailedExeption;
import com.sistemasTarija.dunno.venta.domain.model.Inventario;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto.InventarioRawDTO;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository.InventarioVentaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService implements FindCatalogoUseCase {
    private final InventarioCatalogoVentaPersistancePort catalogoPort;
    private final InventarioPersistancePort inventarioPort;


    @Override
    public List<ResumenPrendaDTO> getListadoGeneral(Integer idSucursal) {
        return catalogoPort.obtenerListadoResumen(idSucursal);
    }

    @Override
    public DetallePrendaDTO getDetalleModelo(Integer idModelo, Integer idSucursal) {
        List<InventarioRawDTO> rawList = catalogoPort.obtenerDetalleModeloRaw(idSucursal, idModelo);

        if (rawList.isEmpty()) {
            throw new InventarioFailedExeption("El modelo solicitado no existe o no tiene stock disponible en esta sucursal.");
        }

        // 2. Extraer cabecera (Tomamos el primer registro porque los datos del modelo se repiten en todos)
        InventarioRawDTO cabecera = rawList.get(0);

        DetallePrendaDTO detalleDTO = new DetallePrendaDTO();
        detalleDTO.setIdModelo(cabecera.getIdModelo());
        detalleDTO.setNombreModelo(cabecera.getNombreModelo());
        detalleDTO.setPrecio(cabecera.getPrecio());
        detalleDTO.setNombreMarca(cabecera.getNombreMarca());
        detalleDTO.setNombreCategoria(cabecera.getNombreCategoria());
        detalleDTO.setNombreCorte(cabecera.getNombreCorte());


        Map<String, List<InventarioRawDTO>> variantesPorColor = rawList.stream()
                .collect(Collectors.groupingBy(InventarioRawDTO::getNombreColor));

        List<ColorDTO> listaColores = new ArrayList<>();

        variantesPorColor.forEach((nombreColor, listaVariantesDelColor) -> {

            InventarioRawDTO infoVisualColor = listaVariantesDelColor.get(0);

            ColorDTO colorDTO = new ColorDTO();
            colorDTO.setNombreColor(nombreColor);
            colorDTO.setCodigoHex(infoVisualColor.getCodigoHex());
            colorDTO.setFotoUrl(infoVisualColor.getFotoUrl());


            List<TallaDTO> listaTallas = listaVariantesDelColor.stream()
                    .map(item -> new TallaDTO(
                            item.getIdVariante(),  // Este es el ID que el front mandará al vender
                            item.getNombreTalla(), // "S", "M", "L"
                            item.getStock()        // "40", "15"
                    ))
                    .collect(Collectors.toList());

            colorDTO.setTallas(listaTallas);

            listaColores.add(colorDTO);
        });
        detalleDTO.setColores(listaColores);

        return detalleDTO;
    }

    /**
     * Registra un ingreso de mercadería al inventario.
     * Si existe el registro (Variante + Sucursal), suma la cantidad.
     * Si no existe, crea un nuevo registro.
     */
    @Transactional
    public void registrarIngreso(Integer idVariante, Integer idSucursal, Integer cantidad) {
        if (cantidad <= 0) {
            throw new InventarioFailedExeption("La cantidad a ingresar debe ser mayor a 0");
        }

        Optional<Inventario> optionalInventario = inventarioPort.findByIdVarianteAndIdSucursal(idVariante, idSucursal);

        if (optionalInventario.isPresent()) {
            // Ya existe: actualizar stock
            Inventario inventario = optionalInventario.get();
            inventario.increaseStock(cantidad);
            inventarioPort.save(inventario);
        } else {
            // No existe: crear nuevo registro
            Inventario nuevoInventario = new Inventario(null, idVariante, cantidad, idSucursal);
            inventarioPort.save(nuevoInventario);
        }
    }

    /**
     * Revierte un ingreso de mercadería (para anulaciones).
     * Resta la cantidad del stock. Valida que no quede negativo.
     */
    @Transactional
    public void revertirIngreso(Integer idVariante, Integer idSucursal, Integer cantidad) {
        if (cantidad <= 0) {
            throw new InventarioFailedExeption("La cantidad a revertir debe ser mayor a 0");
        }

        Inventario inventario = inventarioPort.findByIdVarianteAndIdSucursal(idVariante, idSucursal)
                .orElseThrow(() -> new InventarioFailedExeption(
                        "No se puede revertir: No existe inventario para Variante " + idVariante + 
                        " en Sucursal " + idSucursal));

        // decreaseStock ya valida que no quede negativo
        inventario.decreaseStock(cantidad);
        inventarioPort.save(inventario);
    }
}
