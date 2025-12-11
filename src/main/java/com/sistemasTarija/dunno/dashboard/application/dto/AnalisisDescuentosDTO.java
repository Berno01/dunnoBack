package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.*;

/**
 * DTO para el análisis de descuentos aplicados
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalisisDescuentosDTO {
    
    /**
     * Total descontado en Bolivianos (suma de todos los descuentos)
     */
    private Double totalDescontado;
    
    /**
     * Cantidad de descuentos aplicados (transacciones con descuento > 0)
     */
    private Long cantidadDescuentos;
    
    /**
     * Promedio por descuento (Total Descontado / Cantidad de Descuentos)
     */
    private Double promedioPorDescuento;
    
    /**
     * Porcentaje sobre ventas brutas (Total Descontado / Total Ventas Brutas * 100)
     */
    private Double porcentajeSobreVentasBrutas;
}
