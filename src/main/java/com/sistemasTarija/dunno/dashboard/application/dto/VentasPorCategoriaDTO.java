package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VentasPorCategoriaDTO {
    private String categoria;
    private Long cantidad;
}
