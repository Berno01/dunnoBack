package com.sistemasTarija.dunno.inventario.application.dto;

import lombok.*;

/**
 * DTO para el listado general de inventario (Endpoint 1)
 * Representa el resumen de cada modelo con su stock total global
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventarioResumenDTO {
    private Integer idModelo;
    private String nombreModelo;
    private String fotoPortada;
    private String categoria;
    private String marca;
    private String corte;
    private Long totalStockGlobal;  // SUM(i.cantidad) de todas las variantes en todas las sucursales
}
