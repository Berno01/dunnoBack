package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.mapper;

import com.sistemasTarija.dunno.venta.domain.model.Usuario;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.UsuarioVentaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioVentaPersistanceMapper {

    UsuarioVentaEntity toEntity(Usuario domain);

    Usuario toDomain(UsuarioVentaEntity entity);
}