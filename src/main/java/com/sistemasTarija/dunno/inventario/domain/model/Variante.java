package com.sistemasTarija.dunno.inventario.domain.model;

import com.sistemasTarija.dunno.inventario.domain.model.options.Talla;
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
