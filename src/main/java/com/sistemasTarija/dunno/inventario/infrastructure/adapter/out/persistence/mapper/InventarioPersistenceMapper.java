package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.mapper;

import com.sistemasTarija.dunno.inventario.domain.model.Inventario;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.InventarioEntity;
import org.springframework.stereotype.Component;

@Component
public class InventarioPersistenceMapper {
    
    public Inventario toDomain(InventarioEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Inventario.builder()
                .idInventario(entity.getIdInventario())
                .idVariante(entity.getVariante() != null ? entity.getVariante().getId() : null)
                .stock(entity.getStockInventario())
                .idSucursal(entity.getIdSucursal())
                .estado(entity.getEstado())
                .build();
    }
    
    public InventarioEntity toEntity(Inventario domain) {
        if (domain == null) {
            return null;
        }
        
        InventarioEntity entity = new InventarioEntity();
        entity.setIdInventario(domain.getIdInventario());
        // La variante se debe setear desde el adaptador
        entity.setIdSucursal(domain.getIdSucursal());
        entity.setStockInventario(domain.getStock());
        entity.setEstado(domain.getEstado());
        
        return entity;
    }
}
