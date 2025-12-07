package com.sistemasTarija.dunno.venta.application.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TallaDTO {
    private Integer idVariante; // El ID que se manda al carrito
    private String nombreTalla;
    private Integer stock;
}