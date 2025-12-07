package com.sistemasTarija.dunno.recepcion.application.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TallaDTO {
    @JsonProperty("idVariante")
    private Integer idVariante; // ID que se enviará al crear la recepción
    
    @JsonProperty("nombreTalla")
    private String nombreTalla; // "S", "M", "L", etc.
}
