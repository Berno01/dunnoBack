package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SucursalRepository extends JpaRepository<SucursalEntity, Integer> {
    
    List<SucursalEntity> findAllByEstadoTrue();
}
