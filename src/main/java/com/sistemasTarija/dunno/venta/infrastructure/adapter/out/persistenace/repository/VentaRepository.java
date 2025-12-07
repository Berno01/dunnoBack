package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository;

import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<VentaEntity, Long> {

    @Query("SELECT v FROM VentaEntity v " +
            "LEFT JOIN FETCH v.detalleVenta dv " +
            "LEFT JOIN FETCH dv.variante var " +
            "LEFT JOIN FETCH var.modeloColor mc " +
            "LEFT JOIN FETCH mc.modelo " +
            "WHERE v.id_venta = :idVenta " +
            "AND (:idSucursal IS NULL OR v.idSucursal = :idSucursal)")
    Optional<VentaEntity> buscarPorIdYSucursal(
            @Param("idVenta") Integer idVenta,
            @Param("idSucursal") Integer idSucursal
    );

    @Query("SELECT DISTINCT v FROM VentaEntity v " +
            "LEFT JOIN FETCH v.detalleVenta dv " +
            "LEFT JOIN FETCH dv.variante var " +
            "LEFT JOIN FETCH var.modeloColor mc " +
            "LEFT JOIN FETCH mc.modelo " +
            "WHERE (:idSucursal IS NULL OR v.idSucursal = :idSucursal) " +
            "AND v.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "AND v.estadoVenta = true " +
            "ORDER BY v.fecha DESC")
    List<VentaEntity> buscarListaConFiltros(
            @Param("idSucursal") Integer idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}