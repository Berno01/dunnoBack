package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.MarcaCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<MarcaCatalogoEntity, Integer> {
    Optional<MarcaCatalogoEntity> findByNombre(String nombre);
    List<MarcaCatalogoEntity> findByEstadoTrue();
}
