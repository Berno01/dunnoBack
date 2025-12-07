package com.sistemasTarija.dunno.recepcion.application.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DetalleModeloDTO {
    @JsonProperty("idModelo")
    private Integer idModelo;
    
    @JsonProperty("nombreModelo")
    private String nombreModelo;
    
    @JsonProperty("nombreMarca")
    private String nombreMarca;
    
    @JsonProperty("nombreCategoria")
    private String nombreCategoria;
    
    @JsonProperty("nombreCorte")
    private String nombreCorte;
    
    @JsonProperty("colores")
    private List<ColorDTO> colores = new ArrayList<>();
}
