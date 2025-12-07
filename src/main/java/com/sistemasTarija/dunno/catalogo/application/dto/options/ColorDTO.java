package com.sistemasTarija.dunno.catalogo.application.dto.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * DTO específico para Color (incluye el código hexadecimal)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColorDTO {
    private Integer id;
    private String nombre;
    @JsonProperty("codigoHex")
    private String codigoHex;
}
