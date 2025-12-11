package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.*;

import java.util.List;

/**
 * DTO principal que contiene todas las secciones del dashboard de vendedores
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSalesRepResponseDTO {
    
    /**
     * Sección 1: Ranking de vendedores ordenado por total vendido
     */
    private List<RankingVendedorDTO> rankingVendedores;
    
    /**
     * Sección 2: Análisis de descuentos aplicados
     */
    private AnalisisDescuentosDTO analisisDescuentos;
    
    /**
     * Sección 3: Top categorías más vendidas
     */
    private List<TopItemDTO> topCategorias;
    
    /**
     * Sección 4: Top modelos más vendidos
     */
    private List<TopItemDTO> topModelos;
    
    /**
     * Sección 5: Top colores más vendidos
     */
    private List<TopItemDTO> topColores;
    
    /**
     * Sección 6: Distribución de tallas vendidas
     */
    private List<TopItemDTO> distribucionTallas;
}
