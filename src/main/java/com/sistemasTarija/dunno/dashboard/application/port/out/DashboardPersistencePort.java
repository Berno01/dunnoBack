package com.sistemasTarija.dunno.dashboard.application.port.out;

import com.sistemasTarija.dunno.dashboard.application.dto.*;

import java.util.List;

/**
 * Port de salida para acceder a los datos del dashboard.
 * Define el contrato para obtener métricas y estadísticas de ventas.
 */
public interface DashboardPersistencePort {
    
    /**
     * Obtiene el ranking de vendedores ordenado por total vendido
     *
     * @param filter Filtros a aplicar (fechas, vendedor específico, sucursal)
     * @return Lista de vendedores con sus métricas
     */
    List<RankingVendedorDTO> getRankingVendedores(DashboardFilterDTO filter);
    
    /**
     * Obtiene el análisis de descuentos aplicados
     *
     * @param filter Filtros a aplicar (fechas, vendedor específico, sucursal)
     * @return DTO con métricas de descuentos
     */
    AnalisisDescuentosDTO getAnalisisDescuentos(DashboardFilterDTO filter);
    
    /**
     * Obtiene las categorías más vendidas
     *
     * @param filter Filtros a aplicar (fechas, vendedor específico, sucursal)
     * @param limit Cantidad máxima de resultados a devolver
     * @return Lista de categorías ordenadas por cantidad vendida
     */
    List<TopItemDTO> getTopCategorias(DashboardFilterDTO filter, int limit);
    
    /**
     * Obtiene los modelos más vendidos
     *
     * @param filter Filtros a aplicar (fechas, vendedor específico, sucursal)
     * @param limit Cantidad máxima de resultados a devolver
     * @return Lista de modelos ordenados por cantidad vendida
     */
    List<TopItemDTO> getTopModelos(DashboardFilterDTO filter, int limit);
    
    /**
     * Obtiene los colores más vendidos
     *
     * @param filter Filtros a aplicar (fechas, vendedor específico, sucursal)
     * @param limit Cantidad máxima de resultados a devolver
     * @return Lista de colores ordenados por cantidad vendida
     */
    List<TopItemDTO> getTopColores(DashboardFilterDTO filter, int limit);
    
    /**
     * Obtiene la distribución de tallas vendidas
     *
     * @param filter Filtros a aplicar (fechas, vendedor específico, sucursal)
     * @param limit Cantidad máxima de resultados a devolver
     * @return Lista de tallas ordenadas por cantidad vendida
     */
    List<TopItemDTO> getDistribucionTallas(DashboardFilterDTO filter, int limit);
}
