package com.sistemasTarija.dunno.catalogo.application.dto;

import com.sistemasTarija.dunno.catalogo.application.dto.options.ColorDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.options.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModeloListadoDTO {
    private Integer id;
    private String nombre;
    private Double precio;
    private OptionDTO marca;
    private OptionDTO categoria;
    private OptionDTO corte;
    private List<ModeloColorListadoDTO> colores;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModeloColorListadoDTO {
        private Integer id;
        private String fotoUrl;
        private ColorDTO color;
    }
}
