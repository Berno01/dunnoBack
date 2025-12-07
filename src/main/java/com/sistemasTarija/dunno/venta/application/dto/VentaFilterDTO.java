package com.sistemasTarija.dunno.venta.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaFilterDTO {
    private Integer idSucursal;
    private LocalDate fecha;
}