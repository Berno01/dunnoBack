package com.sistemasTarija.dunno.inventario.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sucursal {
    private Integer id;
    private String nombre;
    private Boolean estado;
}
