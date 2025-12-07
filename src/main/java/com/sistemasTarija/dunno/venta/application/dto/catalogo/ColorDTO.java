package com.sistemasTarija.dunno.venta.application.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ColorDTO {
    private String nombreColor;
    private String codigoHex;
    private String fotoUrl;
    private List<TallaDTO> tallas = new ArrayList<>();
}