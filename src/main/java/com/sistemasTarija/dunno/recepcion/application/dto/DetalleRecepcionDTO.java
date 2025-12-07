package com.sistemasTarija.dunno.recepcion.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleRecepcionDTO {
    @JsonProperty("idDetalleRecepcion")
    private Long idDetalleRecepcion;

    @JsonProperty("idVariante")
    private Integer idVariante;

    @JsonProperty("cantidad")
    private Integer cantidad;

    @JsonProperty("idModelo")
    private Integer idModelo;
}
