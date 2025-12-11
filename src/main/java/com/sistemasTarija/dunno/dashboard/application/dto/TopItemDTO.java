package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.*;

/**
 * DTO genérico para representar items en rankings (categorías, modelos, colores, tallas)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopItemDTO {
    
    /**
     * ID del item (puede ser idCategoria, idModelo, idColor, idTalla)
     */
    private Integer id;
    
    /**
     * Nombre del item
     */
    private String nombre;
    
    /**
     * Cantidad de unidades vendidas
     */
    private Long cantidad;
    
    /**
     * Porcentaje respecto al total
     */
    private Double porcentaje;
    
    /**
     * Posición en el ranking (1, 2, 3, etc.)
     */
    private Integer posicion;
    
    /**
     * Campo opcional para el código hex del color (solo usado para colores)
     */
    private String codigoHex;
}
