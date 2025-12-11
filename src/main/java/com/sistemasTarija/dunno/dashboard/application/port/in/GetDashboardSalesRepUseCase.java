package com.sistemasTarija.dunno.dashboard.application.port.in;

import com.sistemasTarija.dunno.dashboard.application.dto.DashboardFilterDTO;
import com.sistemasTarija.dunno.dashboard.application.dto.DashboardSalesRepResponseDTO;

/**
 * Port de entrada para obtener datos del dashboard de vendedores.
 * Define el caso de uso principal del dashboard.
 */
public interface GetDashboardSalesRepUseCase {
    
    /**
     * Obtiene todos los datos del dashboard de vendedores aplicando los filtros especificados.
     * 
     * Este método coordina la obtención de todas las secciones del dashboard:
     * - Ranking de vendedores
     * - Análisis de descuentos
     * - Top categorías
     * - Top modelos
     * - Top colores
     * - Distribución de tallas
     * 
     * La implementación debe ser eficiente, minimizando las consultas a la base de datos
     * y aplicando los filtros de forma consistente en todas las secciones.
     *
     * @param filter Filtros a aplicar (fechas, vendedor específico, sucursal)
     * @return DTO con todas las secciones del dashboard pobladas
     */
    DashboardSalesRepResponseDTO getDashboardData(DashboardFilterDTO filter);
}
