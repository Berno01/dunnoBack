package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.mapper;

import com.sistemasTarija.dunno.catalogo.domain.model.Modelo;
import com.sistemasTarija.dunno.catalogo.domain.model.ModeloColor;
import com.sistemasTarija.dunno.catalogo.domain.model.Variante;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloColorCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.VarianteCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CatalogoPersistenceMapper.class})
public interface ModeloPersistenceMapper {

    @Mapping(target = "idMarca", source = "idMarca")
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "idCategoria", source = "idCategoria")
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "idCorte", source = "idCorte")
    @Mapping(target = "corte", ignore = true)
    ModeloCatalogoEntity toEntity(Modelo modelo);

    @Mapping(target = "idColor", source = "idColor")
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "modelo", ignore = true)
    ModeloColorCatalogoEntity toEntity(ModeloColor modeloColor);

    @Mapping(target = "idTalla", source = "idTalla")
    @Mapping(target = "talla", ignore = true)
    @Mapping(target = "modeloColor", ignore = true)
    VarianteCatalogoEntity toEntity(Variante variante);

    // Reverse mapping
    @Mapping(target = "idMarca", source = "idMarca")
    @Mapping(target = "idCategoria", source = "idCategoria")
    @Mapping(target = "idCorte", source = "idCorte")
    @Mapping(target = "marca", source = "marca")
    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "corte", source = "corte")
    Modelo toDomain(ModeloCatalogoEntity entity);

    @Mapping(target = "idColor", source = "idColor")
    @Mapping(target = "color", source = "color")
    ModeloColor toDomain(ModeloColorCatalogoEntity entity);

    @Mapping(target = "idTalla", source = "idTalla")
    @Mapping(target = "talla", source = "talla")
    Variante toDomain(VarianteCatalogoEntity entity);

    @AfterMapping
    default void linkModeloColor(@MappingTarget ModeloCatalogoEntity modeloEntity) {
        if (modeloEntity.getColores() != null) {
            modeloEntity.getColores().forEach(color -> {
                color.setModelo(modeloEntity);
                linkVariante(color);
            });
        }
    }

    @AfterMapping
    default void linkVariante(@MappingTarget ModeloColorCatalogoEntity modeloColorEntity) {
        if (modeloColorEntity.getVariantes() != null) {
            modeloColorEntity.getVariantes().forEach(variante -> variante.setModeloColor(modeloColorEntity));
        }
    }
}
