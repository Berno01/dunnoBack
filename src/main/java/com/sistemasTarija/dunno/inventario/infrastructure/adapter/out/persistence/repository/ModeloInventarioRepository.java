package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo.ModeloInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ModeloInventarioRepository extends JpaRepository<ModeloInventarioEntity, Integer> {
    
    /**
     * Busca un modelo con todos sus colores y variantes cargadas
     * Usa FETCH JOIN para evitar N+1 queries
     */
    @Query("SELECT DISTINCT m FROM ModeloInventarioEntity m " +
            "LEFT JOIN FETCH m.marca " +
            "LEFT JOIN FETCH m.categoria " +
            "LEFT JOIN FETCH m.corte " +
            "WHERE m.id = :idModelo " +
            "AND m.estado = true")
    Optional<ModeloInventarioEntity> findByIdWithRelations(@Param("idModelo") Integer idModelo);
}
