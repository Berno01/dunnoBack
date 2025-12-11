package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository JPA para consultas del Dashboard.
 * Utiliza JpaSpecificationExecutor para queries dinámicas con Specifications.
 */
@Repository
public interface DashboardVentaRepository extends JpaRepository<VentaEntity, Integer>, 
                                                   JpaSpecificationExecutor<VentaEntity> {
    
    /**
     * Obtiene el ranking de vendedores con métricas agregadas.
     * Optimizado con una sola query que aplica todos los filtros.
     *
     * @param startDate Fecha inicio (null si no aplica)
     * @param endDate Fecha fin (null si no aplica)
     * @param salesRepId ID del vendedor (null para todos)
     * @param idSucursal ID de sucursal (null para todas)
     * @return Lista de arrays con: [idUsuario, totalVendido, cantidadVentas]
     */
    @Query("""
        SELECT v.createdBy, SUM(v.total), COUNT(v)
        FROM VentaEntity v
        WHERE v.estadoVenta = true
        AND (:startDate IS NULL OR v.fecha >= :startDate)
        AND (:endDate IS NULL OR v.fecha <= :endDate)
        AND (:salesRepId IS NULL OR v.createdBy = :salesRepId)
        AND (:idSucursal IS NULL OR v.idSucursal = :idSucursal)
        GROUP BY v.createdBy
        ORDER BY SUM(v.total) DESC
        """)
    List<Object[]> getRankingVendedores(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("salesRepId") Integer salesRepId,
        @Param("idSucursal") Integer idSucursal
    );
    
    /**
     * Obtiene métricas de descuentos aplicados.
     *
     * @return Object que contiene un array con: [totalDescontado, cantidadDescuentos, totalVentasBrutas]
     */
    @Query("""
        SELECT 
            COALESCE(SUM(v.descuento), 0),
            COUNT(CASE WHEN v.descuento > 0 THEN 1 END),
            COALESCE(SUM(v.total + v.descuento), 0)
        FROM VentaEntity v
        WHERE v.estadoVenta = true
        AND (:startDate IS NULL OR v.fecha >= :startDate)
        AND (:endDate IS NULL OR v.fecha <= :endDate)
        AND (:salesRepId IS NULL OR v.createdBy = :salesRepId)
        AND (:idSucursal IS NULL OR v.idSucursal = :idSucursal)
        """)
    Object getAnalisisDescuentos(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("salesRepId") Integer salesRepId,
        @Param("idSucursal") Integer idSucursal
    );
    
    /**
     * Obtiene el top de categorías más vendidas.
     *
     * @return Lista de arrays con: [idCategoria, nombreCategoria, cantidadUnidades]
     */
    @Query("""
        SELECT 
            cat.id,
            cat.nombre,
            SUM(dv.cantidad)
        FROM VentaEntity v
        JOIN v.detalleVenta dv
        JOIN dv.variante var
        JOIN var.modeloColor mc
        JOIN mc.modelo m
        JOIN m.categoria cat
        WHERE v.estadoVenta = true
        AND (:startDate IS NULL OR v.fecha >= :startDate)
        AND (:endDate IS NULL OR v.fecha <= :endDate)
        AND (:salesRepId IS NULL OR v.createdBy = :salesRepId)
        AND (:idSucursal IS NULL OR v.idSucursal = :idSucursal)
        GROUP BY cat.id, cat.nombre
        ORDER BY SUM(dv.cantidad) DESC
        """)
    List<Object[]> getTopCategorias(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("salesRepId") Integer salesRepId,
        @Param("idSucursal") Integer idSucursal
    );
    
    /**
     * Obtiene el top de modelos más vendidos.
     *
     * @return Lista de arrays con: [idModelo, nombreModelo, cantidadUnidades]
     */
    @Query("""
        SELECT 
            m.id,
            m.nombre,
            SUM(dv.cantidad)
        FROM VentaEntity v
        JOIN v.detalleVenta dv
        JOIN dv.variante var
        JOIN var.modeloColor mc
        JOIN mc.modelo m
        WHERE v.estadoVenta = true
        AND (:startDate IS NULL OR v.fecha >= :startDate)
        AND (:endDate IS NULL OR v.fecha <= :endDate)
        AND (:salesRepId IS NULL OR v.createdBy = :salesRepId)
        AND (:idSucursal IS NULL OR v.idSucursal = :idSucursal)
        GROUP BY m.id, m.nombre
        ORDER BY SUM(dv.cantidad) DESC
        """)
    List<Object[]> getTopModelos(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("salesRepId") Integer salesRepId,
        @Param("idSucursal") Integer idSucursal
    );
    
    /**
     * Obtiene el top de colores más vendidos.
     *
     * @return Lista de arrays con: [idColor, nombreColor, codigoHex, cantidadUnidades]
     */
    @Query("""
        SELECT 
            c.id,
            c.nombre,
            c.codigoHex,
            SUM(dv.cantidad)
        FROM VentaEntity v
        JOIN v.detalleVenta dv
        JOIN dv.variante var
        JOIN var.modeloColor mc
        JOIN mc.color c
        WHERE v.estadoVenta = true
        AND (:startDate IS NULL OR v.fecha >= :startDate)
        AND (:endDate IS NULL OR v.fecha <= :endDate)
        AND (:salesRepId IS NULL OR v.createdBy = :salesRepId)
        AND (:idSucursal IS NULL OR v.idSucursal = :idSucursal)
        GROUP BY c.id, c.nombre, c.codigoHex
        ORDER BY SUM(dv.cantidad) DESC
        """)
    List<Object[]> getTopColores(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("salesRepId") Integer salesRepId,
        @Param("idSucursal") Integer idSucursal
    );
    
    /**
     * Obtiene la distribución de tallas vendidas.
     *
     * @return Lista de arrays con: [idTalla, nombreTalla, cantidadUnidades]
     */
    @Query("""
        SELECT 
            t.id,
            t.nombre,
            SUM(dv.cantidad)
        FROM VentaEntity v
        JOIN v.detalleVenta dv
        JOIN dv.variante var
        JOIN var.talla t
        WHERE v.estadoVenta = true
        AND (:startDate IS NULL OR v.fecha >= :startDate)
        AND (:endDate IS NULL OR v.fecha <= :endDate)
        AND (:salesRepId IS NULL OR v.createdBy = :salesRepId)
        AND (:idSucursal IS NULL OR v.idSucursal = :idSucursal)
        GROUP BY t.id, t.nombre
        ORDER BY SUM(dv.cantidad) DESC
        """)
    List<Object[]> getDistribucionTallas(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("salesRepId") Integer salesRepId,
        @Param("idSucursal") Integer idSucursal
    );
}
