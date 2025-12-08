package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.inventario.application.dto.InventarioResumenDTO;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventarioRepository extends JpaRepository<InventarioEntity, Integer> {
    
    /**
     * Query optimizada: Calcula el stock total global por modelo directamente en BD
     * Usa SUM agregada para evitar traer todas las filas a Java
     */
    @Query("SELECT new com.sistemasTarija.dunno.inventario.application.dto.InventarioResumenDTO(" +
            "m.id, " +                                    // idModelo
            "m.nombre, " +                                // nombreModelo
            "(SELECT MIN(mc2.fotoUrl) FROM ModeloColorInventarioEntity mc2 WHERE mc2.modelo.id = m.id), " + // fotoPortada
            "COALESCE(cat.nombre, ''), " +                // categoria
            "COALESCE(ma.nombre, ''), " +                 // marca
            "COALESCE(co.nombre, ''), " +                 // corte
            "COALESCE(SUM(i.stockInventario), 0L)) " +    // totalStockGlobal
            "FROM ModeloInventarioEntity m " +
            "LEFT JOIN m.marca ma " +
            "LEFT JOIN m.categoria cat " +
            "LEFT JOIN m.corte co " +
            "LEFT JOIN ModeloColorInventarioEntity mc ON mc.modelo.id = m.id " +
            "LEFT JOIN VarianteInventarioEntity v ON v.modeloColor.id = mc.id " +
            "LEFT JOIN InventarioEntity i ON i.variante.id = v.id AND i.estado = true " +
            "GROUP BY m.id, m.nombre, cat.nombre, ma.nombre, co.nombre " +
            "ORDER BY m.nombre")
    List<InventarioResumenDTO> obtenerResumenGlobal();
    
    /**
     * Busca todos los registros de inventario de un modelo específico
     */
    @Query("SELECT i FROM InventarioEntity i " +
            "JOIN i.variante v " +
            "JOIN v.modeloColor mc " +
            "WHERE mc.modelo.id = :idModelo " +
            "AND i.estado = true")
    List<InventarioEntity> findInventariosByModelo(@Param("idModelo") Integer idModelo);
}
