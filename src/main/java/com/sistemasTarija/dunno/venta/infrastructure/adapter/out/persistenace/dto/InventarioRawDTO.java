package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventarioRawDTO {
    // Datos del Modelo
    private Integer idModelo;
    private String nombreModelo;
    private String nombreMarca;
    private String nombreCategoria;
    private String nombreCorte;

    // Datos de la Variante Visual (Color)
    private String nombreColor;
    private String codigoHex;
    private String fotoUrl;

    // Datos de Inventario (Talla y Stock)
    private Integer idVariante;
    private String nombreTalla;
    private Integer stock;
}