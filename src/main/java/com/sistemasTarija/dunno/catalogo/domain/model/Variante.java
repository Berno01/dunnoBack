package com.sistemasTarija.dunno.catalogo.domain.model;
import com.sistemasTarija.dunno.catalogo.domain.model.options.Talla;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Variante {
    private Integer id;
    private Integer idTalla;
    private Talla talla;
}
