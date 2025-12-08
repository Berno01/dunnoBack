package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentasPorHoraDTO {
    private Integer hora;
    private Double cantidad; // Promedio de ventas por hora (puede ser decimal por el promedio de días)
}
