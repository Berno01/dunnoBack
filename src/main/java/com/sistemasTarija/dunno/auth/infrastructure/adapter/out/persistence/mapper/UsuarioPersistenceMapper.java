package com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence.mapper;

import com.sistemasTarija.dunno.auth.domain.model.Usuario;
import com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {
    
    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Usuario.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .rol(entity.getRol())
                .idSucursal(entity.getIdSucursal())
                .nombreCompleto(entity.getNombreCompleto())
                .build();
    }
    
    public UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }
        
        return UsuarioEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .rol(domain.getRol())
                .idSucursal(domain.getIdSucursal())
                .nombreCompleto(domain.getNombreCompleto())
                .build();
    }
}
