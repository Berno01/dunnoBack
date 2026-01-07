package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.repository;

import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.entity.RecepcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecepcionRepository extends JpaRepository<RecepcionEntity, Integer> {
    
    @Query("SELECT r FROM RecepcionEntity r " +
           "WHERE (:idSucursal IS NULL OR r.idSucursal = :idSucursal) " +
           "AND r.idRecepcion = :idRecepcion " +
           "AND r.estado = true")
    Optional<RecepcionEntity> buscarPorIdYSucursal(
            @Param("idRecepcion") Integer idRecepcion,
            @Param("idSucursal") Integer idSucursal
    );

    @Query("SELECT r FROM RecepcionEntity r " +
           "WHERE (:idSucursal IS NULL OR r.idSucursal = :idSucursal) " +
           "AND r.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "AND r.estado = true " +
           "ORDER BY r.fecha DESC")
    List<RecepcionEntity> buscarListaConFiltros(
            @Param("idSucursal") Integer idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT r.idRecepcion as idRecepcion, r.fecha as fecha, r.idSucursal as idSucursal, " +
           "r.estado as estado, d.cantidad as cantidad, " +
           "ma.nombre as nombreMarca, cat.nombre as nombreCategoria " +
           "FROM RecepcionEntity r " +
           "LEFT JOIN r.detalles d " +
           "LEFT JOIN d.variante v " +
           "LEFT JOIN v.modeloColor mc " +
           "LEFT JOIN mc.modelo m " +
           "LEFT JOIN m.marca ma " +
           "LEFT JOIN m.categoria cat " +
           "WHERE (:idSucursal IS NULL OR r.idSucursal = :idSucursal) " +
           "AND r.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "AND r.estado = true " +
           "ORDER BY r.fecha DESC, r.idRecepcion, d.idDetalleRecepcion ASC")
    List<RecepcionResumenProjection> buscarResumenConFiltros(
            @Param("idSucursal") Integer idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
