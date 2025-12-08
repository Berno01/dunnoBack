package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardKPIsDTO {
    private Double totalVentas;
    private Long cantidadVentas;
    private Double ticketPromedio;
    private Long unidadesVendidas;
}
