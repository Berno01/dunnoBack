package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloRepository extends JpaRepository<ModeloCatalogoEntity, Integer> {
    boolean existsByNombre(String nombre);
}
