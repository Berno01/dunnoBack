package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo.ModeloColorInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModeloColorInventarioRepository extends JpaRepository<ModeloColorInventarioEntity, Integer> {
    
    /**
     * Busca todos los colores de un modelo con sus variantes
     */
    @Query("SELECT DISTINCT mc FROM ModeloColorInventarioEntity mc " +
            "LEFT JOIN FETCH mc.color " +
            "WHERE mc.modelo.id = :idModelo")
    List<ModeloColorInventarioEntity> findByModeloIdWithColor(@Param("idModelo") Integer idModelo);
}
