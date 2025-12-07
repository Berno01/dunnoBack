package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.TallaCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TallaRepository extends JpaRepository<TallaCatalogoEntity, Integer> {
    Optional<TallaCatalogoEntity> findByNombre(String nombre);
}
