package com.sistemasTarija.dunno.catalogo.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarModeloRequest {
    @JsonProperty("nombreModelo")
    private String nombreModelo;
    @JsonProperty("precio")
    private Double precio;
    @JsonProperty("idMarca")
    private Integer idMarca;
    @JsonProperty("idCategoria")
    private Integer idCategoria;
    @JsonProperty("idCorte")
    private Integer idCorte;
    @JsonProperty("colores")
    private List<ColorRequest> colores;

    @JsonProperty("idsTallas")
    private List<Integer> idsTallas;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ColorRequest {
        @JsonProperty("idColor")
        private Integer idColor;
        @JsonProperty("fotoUrl")
        private String fotoUrl;
    }
}
