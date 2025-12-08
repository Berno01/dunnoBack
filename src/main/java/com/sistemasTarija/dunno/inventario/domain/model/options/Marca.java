package com.sistemasTarija.dunno.inventario.domain.model.options;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Marca {
    private Integer id;
    private String nombre;
    private Boolean estado;
}
