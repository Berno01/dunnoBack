package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.*;

/**
 * DTO para el ranking de vendedores
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingVendedorDTO {
    
    /**
     * ID del usuario/vendedor
     */
    private Integer idUsuario;
    
    /**
     * Nombre completo del vendedor
     */
    private String nombreCompleto;
    
    /**
     * Username del vendedor (para mostrar como "Tarija", etc.)
     */
    private String username;
    
    /**
     * Total monetario vendido en Bolivianos (Bs.)
     */
    private Double totalVendido;
    
    /**
     * Cantidad total de transacciones/ventas realizadas
     */
    private Long cantidadVentas;
    
    /**
     * Posición en el ranking (1, 2, 3, etc.)
     */
    private Integer posicion;
}
