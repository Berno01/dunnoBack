package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModeloRawDTO {
    private Integer idModelo;
    private String nombreModelo;
    private String nombreMarca;
    private String nombreCategoria;
    private String nombreCorte;
    private String nombreColor;
    private String codigoHex;
    private String fotoUrl;
    private Integer idVariante;
    private String nombreTalla;
}
