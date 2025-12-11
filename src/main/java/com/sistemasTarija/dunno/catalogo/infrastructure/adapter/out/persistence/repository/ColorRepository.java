package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.ColorCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<ColorCatalogoEntity, Integer> {
    Optional<ColorCatalogoEntity> findByNombre(String nombre);
    List<ColorCatalogoEntity> findByEstadoTrue();
}
