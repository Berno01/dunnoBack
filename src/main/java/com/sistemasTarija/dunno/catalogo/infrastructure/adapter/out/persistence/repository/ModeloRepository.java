package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<ModeloCatalogoEntity, Integer> {
    boolean existsByNombre(String nombre);
    List<ModeloCatalogoEntity> findByEstadoTrue();
    List<ModeloCatalogoEntity> findByEstadoTrueOrderByIdDesc();
}
