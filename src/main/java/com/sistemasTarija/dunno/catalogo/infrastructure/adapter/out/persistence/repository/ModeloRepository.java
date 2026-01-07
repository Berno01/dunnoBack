package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloCatalogoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<ModeloCatalogoEntity, Integer> {
    boolean existsByNombre(String nombre);
    List<ModeloCatalogoEntity> findByEstadoTrue();
    
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT m FROM ModeloCatalogoEntity m " +
           "LEFT JOIN FETCH m.colores mc " +
           "LEFT JOIN FETCH mc.color " +
           "LEFT JOIN FETCH m.marca " +
           "LEFT JOIN FETCH m.categoria " +
           "LEFT JOIN FETCH m.corte " +
           "WHERE m.estado = true " +
           "ORDER BY m.id DESC")
    List<ModeloCatalogoEntity> findAllWithColores();

    List<ModeloCatalogoEntity> findByEstadoTrueOrderByIdDesc();
}
