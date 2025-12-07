package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.mapper;

import com.sistemasTarija.dunno.recepcion.domain.model.DetalleRecepcion;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.entity.DetalleRecepcionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleRecepcionPersistanceMapper {

    @Mapping(target = "idDetalleRecepcion", ignore = true)
    @Mapping(target = "recepcion", ignore = true)
    @Mapping(target = "variante", ignore = true)
    DetalleRecepcionEntity toDetalleEntity(DetalleRecepcion detalle);

    @Mapping(target = "idModelo", source = "variante.modeloColor.modelo.id")
    DetalleRecepcion toDetalleModel(DetalleRecepcionEntity entity);
}
