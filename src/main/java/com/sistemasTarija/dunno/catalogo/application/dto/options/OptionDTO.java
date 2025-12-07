package com.sistemasTarija.dunno.catalogo.application.dto.options;

import lombok.*;

/**
 * DTO genérico para las opciones del catálogo (Marca, Categoria, Corte, Talla)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDTO {
    private Integer id;
    private String nombre;
}
