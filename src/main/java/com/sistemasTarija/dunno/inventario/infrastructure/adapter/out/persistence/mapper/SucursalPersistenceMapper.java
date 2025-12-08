package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.mapper;

import com.sistemasTarija.dunno.inventario.domain.model.Sucursal;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.springframework.stereotype.Component;

@Component
public class SucursalPersistenceMapper {
    
    public Sucursal toDomain(SucursalEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Sucursal.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .estado(entity.getEstado())
                .build();
    }
    
    public SucursalEntity toEntity(Sucursal domain) {
        if (domain == null) {
            return null;
        }
        
        SucursalEntity entity = new SucursalEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setEstado(domain.getEstado());
        
        return entity;
    }
}
