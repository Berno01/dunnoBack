package com.sistemasTarija.dunno.recepcion.application.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResumenModeloDTO {
    @JsonProperty("idModelo")
    private Integer idModelo;
    
    @JsonProperty("nombreModelo")
    private String nombreModelo;
    
    @JsonProperty("nombreMarca")
    private String nombreMarca;
    
    @JsonProperty("nombreCategoria")
    private String nombreCategoria;
    
    @JsonProperty("fotoPortada")
    private String fotoPortada; // Una sola foto representativa
}
