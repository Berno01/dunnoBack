package com.sistemasTarija.dunno.catalogo.application.dto;

import com.sistemasTarija.dunno.catalogo.application.dto.options.OptionDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.options.ColorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModeloDTO {
    private Integer id;
    private String nombre;
    private Double precio;
    private OptionDTO marca;
    private OptionDTO categoria;
    private OptionDTO corte;
    private List<ModeloColorDTO> colores;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModeloColorDTO {
        private Integer id;
        private String fotoUrl;
        private ColorDTO color;
        private List<VarianteDTO> variantes;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VarianteDTO {
        private Integer id;
        private OptionDTO talla;
    }
}
