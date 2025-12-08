package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo.VarianteInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VarianteInventarioRepository extends JpaRepository<VarianteInventarioEntity, Integer> {
    
    /**
     * Busca todas las variantes de un modelo color específico
     */
    @Query("SELECT v FROM VarianteInventarioEntity v " +
            "LEFT JOIN FETCH v.talla " +
            "WHERE v.modeloColor.id = :idModeloColor")
    List<VarianteInventarioEntity> findByModeloColorIdWithTalla(@Param("idModeloColor") Integer idModeloColor);
}
