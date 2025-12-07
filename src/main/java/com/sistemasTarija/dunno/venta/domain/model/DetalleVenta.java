package com.sistemasTarija.dunno.venta.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DetalleVenta {
    private Integer idVariante;
    private Integer idModelo; // Solo para lectura - enviado al frontend
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;

    public DetalleVenta(Integer idVariante, Integer cantidad, Double precioUnitario) {
        this.idVariante = idVariante;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = precioUnitario * cantidad;
    }


}
