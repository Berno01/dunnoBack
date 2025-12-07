package com.sistemasTarija.dunno.catalogo.domain.model.options;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Color {
    private Integer id;
    private String nombre;
    private String codigoHex;
    private Boolean estado;
}
