package com.sistemasTarija.dunno.inventario.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventario {
    private Integer idInventario;
    private Integer idVariante;
    private Integer stock;
    private Integer idSucursal;
    private Boolean estado;
}
