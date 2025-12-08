package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopProductoDTO {
    private String nombreModelo;
    private String subtitulo;
    private Long cantidadVendida;
    private Long stockActual;
    private String fotoUrl;
}
