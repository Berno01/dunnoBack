package com.sistemasTarija.dunno.recepcion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRecepcion {
    private Long idDetalleRecepcion;
    private Integer idVariante;
    private Integer cantidad;
    private Integer idModelo;
}
