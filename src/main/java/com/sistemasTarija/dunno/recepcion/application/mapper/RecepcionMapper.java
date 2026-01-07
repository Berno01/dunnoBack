package com.sistemasTarija.dunno.recepcion.application.mapper;

import com.sistemasTarija.dunno.recepcion.application.dto.RecepcionDTO;
import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;
import com.sistemasTarija.dunno.recepcion.domain.model.RecepcionResumen;
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
    
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "totalItems", source = "totalItems")
    @Mapping(target = "marca", source = "marca")
    @Mapping(target = "categorias", source = "categorias")
    RecepcionDTO toDto(RecepcionResumen resumen);

    List<RecepcionDTO> toDtoListFromResumen(List<RecepcionResumen> resumenes);
}
