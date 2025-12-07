package com.sistemasTarija.dunno.catalogo.domain.model;
import com.sistemasTarija.dunno.catalogo.domain.model.options.Color;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModeloColor {
    private Integer id;
    private String fotoUrl;
    private Integer idColor;
    private Color color;
    
    @Builder.Default
    private List<Variante> variantes = new ArrayList<>();
}
