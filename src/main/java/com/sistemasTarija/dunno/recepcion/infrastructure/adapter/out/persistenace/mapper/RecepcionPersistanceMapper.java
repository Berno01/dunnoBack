package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.mapper;

import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.entity.RecepcionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DetalleRecepcionPersistanceMapper.class})
public interface RecepcionPersistanceMapper {

    @Mapping(target = "idRecepcion", source = "idRecepcion")
    @Mapping(target = "detalles", source = "detalles")
    RecepcionEntity toRecepcionEntity(Recepcion recepcion);

    @Mapping(target = "idRecepcion", source = "idRecepcion")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "detalles", source = "detalles")
    Recepcion toRecepcionModel(RecepcionEntity entity);

    List<Recepcion> toListRecepcionModel(List<RecepcionEntity> entityList);
}
