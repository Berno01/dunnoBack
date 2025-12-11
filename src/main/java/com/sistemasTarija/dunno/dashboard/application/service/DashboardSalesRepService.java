package com.sistemasTarija.dunno.dashboard.application.service;

import com.sistemasTarija.dunno.dashboard.application.dto.*;
import com.sistemasTarija.dunno.dashboard.application.port.in.GetDashboardSalesRepUseCase;
import com.sistemasTarija.dunno.dashboard.application.port.out.DashboardPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que implementa la lógica de negocio del dashboard de vendedores.
 * 
 * ESTRATEGIA DE OPTIMIZACIÓN:
 * - Utiliza queries JPQL optimizadas con agregaciones (SUM, COUNT, GROUP BY)
 * - Los filtros se aplican una sola vez en cada query mediante parámetros
 * - No se ejecutan queries adicionales cuando se cambia de vendedor
 * - Los JOINs están optimizados para traer solo los datos necesarios
 * - Se minimiza el tráfico de red agrupando datos relacionados
 * 
 * FILTRADO DINÁMICO:
 * - Todos los métodos respetan el filtro base (estado activo + fechas)
 * - Si salesRepId está presente, todos los cálculos se filtran por ese vendedor
 * - Si salesRepId es null, se muestran datos agregados de todos los vendedores
 * - El filtro de sucursal se aplica para usuarios no admin
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardSalesRepService implements GetDashboardSalesRepUseCase {
    
    private final DashboardPersistencePort dashboardPort;
    
    // Límites por defecto para los tops
    private static final int TOP_CATEGORIAS_LIMIT = 5;
    private static final int TOP_MODELOS_LIMIT = 5;
    private static final int TOP_COLORES_LIMIT = 5;
    private static final int TOP_TALLAS_LIMIT = 10;
    
    @Override
    public DashboardSalesRepResponseDTO getDashboardData(DashboardFilterDTO filter) {
        log.info("Obteniendo datos del dashboard con filtros: startDate={}, endDate={}, salesRepId={}, idSucursal={}", 
            filter.getStartDate(), filter.getEndDate(), filter.getSalesRepId(), filter.getIdSucursal());
        
        // OPTIMIZACIÓN: Ejecutamos todas las consultas de forma coordinada
        // pero cada una usa la misma lógica de filtrado, aplicada a nivel de DB
        
        // Sección 1: Ranking de vendedores
        List<RankingVendedorDTO> ranking = dashboardPort.getRankingVendedores(filter);
        log.debug("Ranking de vendedores obtenido: {} vendedores", ranking.size());
        
        // Sección 2: Análisis de descuentos
        AnalisisDescuentosDTO descuentos = dashboardPort.getAnalisisDescuentos(filter);
        log.debug("Análisis de descuentos: {} descuentos aplicados por Bs. {}", 
            descuentos.getCantidadDescuentos(), descuentos.getTotalDescontado());
        
        // Sección 3: Top Categorías
        List<TopItemDTO> topCategorias = dashboardPort.getTopCategorias(filter, TOP_CATEGORIAS_LIMIT);
        log.debug("Top categorías obtenido: {} categorías", topCategorias.size());
        
        // Sección 4: Top Modelos
        List<TopItemDTO> topModelos = dashboardPort.getTopModelos(filter, TOP_MODELOS_LIMIT);
        log.debug("Top modelos obtenido: {} modelos", topModelos.size());
        
        // Sección 5: Top Colores
        List<TopItemDTO> topColores = dashboardPort.getTopColores(filter, TOP_COLORES_LIMIT);
        log.debug("Top colores obtenido: {} colores", topColores.size());
        
        // Sección 6: Distribución de Tallas
        List<TopItemDTO> distribucionTallas = dashboardPort.getDistribucionTallas(filter, TOP_TALLAS_LIMIT);
        log.debug("Distribución de tallas obtenida: {} tallas", distribucionTallas.size());
        
        // Construir el DTO de respuesta completo
        DashboardSalesRepResponseDTO response = DashboardSalesRepResponseDTO.builder()
            .rankingVendedores(ranking)
            .analisisDescuentos(descuentos)
            .topCategorias(topCategorias)
            .topModelos(topModelos)
            .topColores(topColores)
            .distribucionTallas(distribucionTallas)
            .build();
        
        log.info("Dashboard completo obtenido exitosamente");
        return response;
    }
}
