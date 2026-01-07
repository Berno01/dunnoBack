package com.sistemasTarija.dunno.catalogo.application.mapper;

import com.sistemasTarija.dunno.catalogo.application.dto.ModeloDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.ModeloListadoDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.RegistrarModeloRequest;
import com.sistemasTarija.dunno.catalogo.domain.model.Modelo;
import com.sistemasTarija.dunno.catalogo.domain.model.ModeloColor;
import com.sistemasTarija.dunno.catalogo.domain.model.Variante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModeloMapper {

    private final CatalogoMapper catalogoMapper;

    public Modelo toDomain(RegistrarModeloRequest request) {
        if (request == null) return null;

        List<ModeloColor> colores = new ArrayList<>();
        if (request.getColores() != null) {
            colores = request.getColores().stream()
                    .map(this::toModeloColorDomain)
                    .collect(Collectors.toList());
        }

        return Modelo.builder()
                .nombre(request.getNombreModelo())
                .precio(request.getPrecio())
                .idMarca(request.getIdMarca())
                .idCategoria(request.getIdCategoria())
                .idCorte(request.getIdCorte())
                .colores(colores)
                .build();
    }

    private ModeloColor toModeloColorDomain(RegistrarModeloRequest.ColorRequest request) {
        if (request == null) return null;
        return ModeloColor.builder()
                .idColor(request.getIdColor())
                .fotoUrl(request.getFotoUrl())
                .build();
    }

    public ModeloListadoDTO toListadoDTO(Modelo modelo) {
        if (modelo == null) return null;

        List<ModeloListadoDTO.ModeloColorListadoDTO> coloresDTO = new ArrayList<>();
        if (modelo.getColores() != null) {
            coloresDTO = modelo.getColores().stream()
                    .map(this::toModeloColorListadoDTO)
                    .collect(Collectors.toList());
        }

        return ModeloListadoDTO.builder()
                .id(modelo.getId())
                .nombre(modelo.getNombre())
                .precio(modelo.getPrecio())
                .marca(catalogoMapper.toMarcaDTO(modelo.getMarca()))
                .categoria(catalogoMapper.toCategoriaDTO(modelo.getCategoria()))
                .corte(catalogoMapper.toCorteDTO(modelo.getCorte()))
                .colores(coloresDTO)
                .build();
    }

    private ModeloListadoDTO.ModeloColorListadoDTO toModeloColorListadoDTO(ModeloColor modeloColor) {
        if (modeloColor == null) return null;
        
        return ModeloListadoDTO.ModeloColorListadoDTO.builder()
                .id(modeloColor.getId())
                .fotoUrl(modeloColor.getFotoUrl())
                .color(catalogoMapper.toColorDTO(modeloColor.getColor()))
                .build();
    }

    // Response mapping
    public ModeloDTO toDTO(Modelo modelo) {
        if (modelo == null) return null;

        List<ModeloDTO.ModeloColorDTO> coloresDTO = new ArrayList<>();
        if (modelo.getColores() != null) {
            coloresDTO = modelo.getColores().stream()
                    .map(this::toModeloColorDTO)
                    .collect(Collectors.toList());
        }

        return ModeloDTO.builder()
                .id(modelo.getId())
                .nombre(modelo.getNombre())
                .precio(modelo.getPrecio())
                .marca(catalogoMapper.toMarcaDTO(modelo.getMarca()))
                .categoria(catalogoMapper.toCategoriaDTO(modelo.getCategoria()))
                .corte(catalogoMapper.toCorteDTO(modelo.getCorte()))
                .colores(coloresDTO)
                .build();
    }

    private ModeloDTO.ModeloColorDTO toModeloColorDTO(ModeloColor modeloColor) {
        if (modeloColor == null) return null;

        List<ModeloDTO.VarianteDTO> variantesDTO = new ArrayList<>();
        if (modeloColor.getVariantes() != null) {
            variantesDTO = modeloColor.getVariantes().stream()
                    .map(this::toVarianteDTO)
                    .collect(Collectors.toList());
        }

        return ModeloDTO.ModeloColorDTO.builder()
                .id(modeloColor.getId())
                .fotoUrl(modeloColor.getFotoUrl())
                .color(catalogoMapper.toColorDTO(modeloColor.getColor()))
                .variantes(variantesDTO)
                .build();
    }

    private ModeloDTO.VarianteDTO toVarianteDTO(Variante variante) {
        if (variante == null) return null;
        return ModeloDTO.VarianteDTO.builder()
                .id(variante.getId())
                .talla(catalogoMapper.toTallaDTO(variante.getTalla()))
                .build();
    }
}
