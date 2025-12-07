package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.mapper;

import com.sistemasTarija.dunno.catalogo.domain.model.options.*;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.*;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper de Persistencia: convierte Modelos de Dominio <-> Entidades JPA
 */
@Mapper(componentModel = "spring")
public interface CatalogoPersistenceMapper {
    
    // === MARCA ===
    MarcaCatalogoEntity toMarcaEntity(Marca marca);
    Marca toMarcaDomain(MarcaCatalogoEntity entity);
    List<Marca> toMarcaDomainList(List<MarcaCatalogoEntity> entities);
    
    // === CATEGORIA ===
    CategoriaCatalogoEntity toCategoriaEntity(Categoria categoria);
    Categoria toCategoriaDomain(CategoriaCatalogoEntity entity);
    List<Categoria> toCategoriaDomainList(List<CategoriaCatalogoEntity> entities);
    
    // === CORTE ===
    CorteCatalogoEntity toCorteEntity(Corte corte);
    Corte toCorteDomain(CorteCatalogoEntity entity);
    List<Corte> toCorteDomainList(List<CorteCatalogoEntity> entities);
    
    // === TALLA ===
    TallaCatalogoEntity toTallaEntity(Talla talla);
    Talla toTallaDomain(TallaCatalogoEntity entity);
    List<Talla> toTallaDomainList(List<TallaCatalogoEntity> entities);
    
    // === COLOR ===
    ColorCatalogoEntity toColorEntity(Color color);
    Color toColorDomain(ColorCatalogoEntity entity);
    List<Color> toColorDomainList(List<ColorCatalogoEntity> entities);
}
