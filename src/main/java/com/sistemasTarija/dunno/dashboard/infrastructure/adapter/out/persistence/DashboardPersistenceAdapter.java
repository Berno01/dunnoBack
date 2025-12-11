package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence;

import com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.sistemasTarija.dunno.dashboard.application.dto.*;
import com.sistemasTarija.dunno.dashboard.application.port.out.DashboardPersistencePort;
import com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.repository.DashboardUsuarioRepository;
import com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.repository.DashboardVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Adapter de persistencia para el Dashboard.
 * Implementa el port de salida usando los repositories JPA.
 */
@Component
@RequiredArgsConstructor
public class DashboardPersistenceAdapter implements DashboardPersistencePort {
    
    private final DashboardVentaRepository ventaRepository;
    private final DashboardUsuarioRepository usuarioRepository;
    
    @Override
    public List<RankingVendedorDTO> getRankingVendedores(DashboardFilterDTO filter) {
        var startDate = filter.getStartDate() != null ? filter.getStartDate().atStartOfDay() : null;
        var endDate = filter.getEndDate() != null ? filter.getEndDate().atTime(LocalTime.MAX) : null;
        
        List<Object[]> results = ventaRepository.getRankingVendedores(
            startDate,
            endDate,
            filter.getSalesRepId(),
            filter.getIdSucursal()
        );
        
        if (results.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Obtener IDs de usuarios
        List<Integer> userIds = results.stream()
            .map(row -> (Integer) row[0])
            .collect(Collectors.toList());
        
        // Cargar datos de usuarios en una sola query
        Map<Integer, UsuarioEntity> usuariosMap = usuarioRepository.findByIdIn(userIds)
            .stream()
            .collect(Collectors.toMap(UsuarioEntity::getId, u -> u));
        
        // Mapear resultados a DTOs
        List<RankingVendedorDTO> ranking = new ArrayList<>();
        int posicion = 1;
        
        for (Object[] row : results) {
            Integer idUsuario = (Integer) row[0];
            Double totalVendido = ((Number) row[1]).doubleValue();
            Long cantidadVentas = ((Number) row[2]).longValue();
            
            UsuarioEntity usuario = usuariosMap.get(idUsuario);
            
            RankingVendedorDTO dto = RankingVendedorDTO.builder()
                .idUsuario(idUsuario)
                .nombreCompleto(usuario != null ? usuario.getNombreCompleto() : "Desconocido")
                .username(usuario != null ? usuario.getUsername() : "")
                .totalVendido(totalVendido)
                .cantidadVentas(cantidadVentas)
                .posicion(posicion++)
                .build();
            
            ranking.add(dto);
        }
        
        return ranking;
    }
    
    @Override
    public AnalisisDescuentosDTO getAnalisisDescuentos(DashboardFilterDTO filter) {
        var startDate = filter.getStartDate() != null ? filter.getStartDate().atStartOfDay() : null;
        var endDate = filter.getEndDate() != null ? filter.getEndDate().atTime(LocalTime.MAX) : null;
        
        Object result = ventaRepository.getAnalisisDescuentos(
            startDate,
            endDate,
            filter.getSalesRepId(),
            filter.getIdSucursal()
        );
        
        // El resultado es un Object[] cuando JPA retorna múltiples columnas
        Object[] row = (Object[]) result;
        
        Double totalDescontado = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
        Long cantidadDescuentos = row[1] != null ? ((Number) row[1]).longValue() : 0L;
        Double totalVentasBrutas = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
        
        // Calcular métricas derivadas
        Double promedioPorDescuento = cantidadDescuentos > 0 
            ? totalDescontado / cantidadDescuentos 
            : 0.0;
        
        Double porcentajeSobreVentasBrutas = totalVentasBrutas > 0 
            ? (totalDescontado / totalVentasBrutas) * 100 
            : 0.0;
        
        return AnalisisDescuentosDTO.builder()
            .totalDescontado(totalDescontado)
            .cantidadDescuentos(cantidadDescuentos)
            .promedioPorDescuento(promedioPorDescuento)
            .porcentajeSobreVentasBrutas(porcentajeSobreVentasBrutas)
            .build();
    }
    
    @Override
    public List<TopItemDTO> getTopCategorias(DashboardFilterDTO filter, int limit) {
        var startDate = filter.getStartDate() != null ? filter.getStartDate().atStartOfDay() : null;
        var endDate = filter.getEndDate() != null ? filter.getEndDate().atTime(LocalTime.MAX) : null;
        
        List<Object[]> results = ventaRepository.getTopCategorias(
            startDate,
            endDate,
            filter.getSalesRepId(),
            filter.getIdSucursal()
        );
        
        return mapToTopItemDTOs(results, limit, false);
    }
    
    @Override
    public List<TopItemDTO> getTopModelos(DashboardFilterDTO filter, int limit) {
        var startDate = filter.getStartDate() != null ? filter.getStartDate().atStartOfDay() : null;
        var endDate = filter.getEndDate() != null ? filter.getEndDate().atTime(LocalTime.MAX) : null;
        
        List<Object[]> results = ventaRepository.getTopModelos(
            startDate,
            endDate,
            filter.getSalesRepId(),
            filter.getIdSucursal()
        );
        
        return mapToTopItemDTOs(results, limit, false);
    }
    
    @Override
    public List<TopItemDTO> getTopColores(DashboardFilterDTO filter, int limit) {
        var startDate = filter.getStartDate() != null ? filter.getStartDate().atStartOfDay() : null;
        var endDate = filter.getEndDate() != null ? filter.getEndDate().atTime(LocalTime.MAX) : null;
        
        List<Object[]> results = ventaRepository.getTopColores(
            startDate,
            endDate,
            filter.getSalesRepId(),
            filter.getIdSucursal()
        );
        
        return mapToTopItemDTOsConColor(results, limit);
    }
    
    @Override
    public List<TopItemDTO> getDistribucionTallas(DashboardFilterDTO filter, int limit) {
        var startDate = filter.getStartDate() != null ? filter.getStartDate().atStartOfDay() : null;
        var endDate = filter.getEndDate() != null ? filter.getEndDate().atTime(LocalTime.MAX) : null;
        
        List<Object[]> results = ventaRepository.getDistribucionTallas(
            startDate,
            endDate,
            filter.getSalesRepId(),
            filter.getIdSucursal()
        );
        
        return mapToTopItemDTOs(results, limit, false);
    }
    
    /**
     * Mapea resultados de queries a TopItemDTOs con cálculo de porcentajes
     */
    private List<TopItemDTO> mapToTopItemDTOs(List<Object[]> results, int limit, boolean includeAll) {
        if (results.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Calcular total para porcentajes
        long totalUnidades = results.stream()
            .mapToLong(row -> ((Number) row[2]).longValue())
            .sum();
        
        // Aplicar límite si es necesario
        List<Object[]> limitedResults = includeAll ? results : results.stream()
            .limit(limit)
            .collect(Collectors.toList());
        
        List<TopItemDTO> items = new ArrayList<>();
        int posicion = 1;
        
        for (Object[] row : limitedResults) {
            Integer id = (Integer) row[0];
            String nombre = (String) row[1];
            Long cantidad = ((Number) row[2]).longValue();
            Double porcentaje = totalUnidades > 0 
                ? (cantidad * 100.0) / totalUnidades 
                : 0.0;
            
            TopItemDTO dto = TopItemDTO.builder()
                .id(id)
                .nombre(nombre)
                .cantidad(cantidad)
                .porcentaje(Math.round(porcentaje * 100.0) / 100.0) // 2 decimales
                .posicion(posicion++)
                .build();
            
            items.add(dto);
        }
        
        return items;
    }
    
    /**
     * Mapea resultados de colores (incluye código hex)
     */
    private List<TopItemDTO> mapToTopItemDTOsConColor(List<Object[]> results, int limit) {
        if (results.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Calcular total para porcentajes
        long totalUnidades = results.stream()
            .mapToLong(row -> ((Number) row[3]).longValue())
            .sum();
        
        // Aplicar límite
        List<Object[]> limitedResults = results.stream()
            .limit(limit)
            .collect(Collectors.toList());
        
        List<TopItemDTO> items = new ArrayList<>();
        int posicion = 1;
        
        for (Object[] row : limitedResults) {
            Integer id = (Integer) row[0];
            String nombre = (String) row[1];
            String codigoHex = (String) row[2];
            Long cantidad = ((Number) row[3]).longValue();
            Double porcentaje = totalUnidades > 0 
                ? (cantidad * 100.0) / totalUnidades 
                : 0.0;
            
            TopItemDTO dto = TopItemDTO.builder()
                .id(id)
                .nombre(nombre)
                .cantidad(cantidad)
                .porcentaje(Math.round(porcentaje * 100.0) / 100.0)
                .posicion(posicion++)
                .codigoHex(codigoHex)
                .build();
            
            items.add(dto);
        }
        
        return items;
    }
}
