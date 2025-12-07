package com.sistemasTarija.dunno.recepcion.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionFilterDTO {
    @JsonProperty("fecha")
    private LocalDate fecha;

    @JsonProperty("idSucursal")
    private Integer idSucursal;
}
