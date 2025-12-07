package com.sistemasTarija.dunno.recepcion.application.mapper;

import com.sistemasTarija.dunno.recepcion.application.dto.RecepcionDTO;
import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecepcionMapper {

    @Mapping(target = "idRecepcion", source = "idRecepcion")
    Recepcion toDomain(RecepcionDTO dto);

    @Mapping(target = "idRecepcion", source = "idRecepcion")
    RecepcionDTO toDto(Recepcion recepcion);

    List<RecepcionDTO> toDtoList(List<Recepcion> recepciones);
}
