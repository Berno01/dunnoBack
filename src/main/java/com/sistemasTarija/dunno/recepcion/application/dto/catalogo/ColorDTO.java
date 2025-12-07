package com.sistemasTarija.dunno.recepcion.application.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColorDTO {
    @JsonProperty("nombreColor")
    private String nombreColor;
    
    @JsonProperty("codigoHex")
    private String codigoHex;
    
    @JsonProperty("fotoUrl")
    private String fotoUrl;
    
    @JsonProperty("tallas")
    private List<TallaDTO> tallas;
}
