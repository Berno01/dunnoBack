package com.sistemasTarija.dunno.catalogo.domain.model.options;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Corte {
    private Integer id;
    private String nombre;
    private Boolean estado;
}
