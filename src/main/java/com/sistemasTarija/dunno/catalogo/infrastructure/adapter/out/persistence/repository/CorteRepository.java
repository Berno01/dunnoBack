package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.CorteCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorteRepository extends JpaRepository<CorteCatalogoEntity, Integer> {
    Optional<CorteCatalogoEntity> findByNombre(String nombre);
    List<CorteCatalogoEntity> findByEstadoTrue();
}
