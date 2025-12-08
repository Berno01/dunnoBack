package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.repository;

import com.sistemasTarija.dunno.dashboard.application.dto.*;
import com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardRepository extends JpaRepository<VentaEntity, Integer> {

    /**
     * KPIs del Dashboard
     */
    @Query(value = "SELECT " +
           "COALESCE(SUM(v.total_venta), 0.0) as totalVentas, " +
           "COALESCE(COUNT(v.id_venta), 0) as cantidadVentas, " +
           "COALESCE(AVG(v.total_venta), 0.0) as ticketPromedio, " +
           "COALESCE((SELECT SUM(dv.cantidad) " +
           "          FROM detalle_venta dv " +
           "          WHERE dv.id_venta IN (SELECT v2.id_venta " +
           "                                 FROM venta v2 " +
           "                                 WHERE v2.estado_venta = true " +
           "                                 AND (:idSucursal IS NULL OR v2.id_sucursal = :idSucursal) " +
           "                                 AND v2.fecha_venta BETWEEN :fechaInicio AND :fechaFin)), 0) as unidadesVendidas " +
           "FROM venta v " +
           "WHERE v.estado_venta = true " +
           "AND (:idSucursal IS NULL OR v.id_sucursal = :idSucursal) " +
           "AND v.fecha_venta BETWEEN :fechaInicio AND :fechaFin",
           nativeQuery = true)
    Object obtenerKPIsRaw(
            @Param("idSucursal") Long idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Ventas por hora - Cuenta cantidad de ventas por hora (no monto)
     * Sin filtro de horario - trae todas las horas donde hay ventas
     */
    @Query(value = "SELECT HOUR(v.fecha_venta) as hora, " +
           "COUNT(v.id_venta) as cantidadVentas, " +
           "COUNT(DISTINCT DATE(v.fecha_venta)) as diasUnicos " +
           "FROM venta v " +
           "WHERE v.estado_venta = true " +
           "AND (:idSucursal IS NULL OR v.id_sucursal = :idSucursal) " +
           "AND v.fecha_venta BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY HOUR(v.fecha_venta) " +
           "ORDER BY HOUR(v.fecha_venta)",
           nativeQuery = true)
    List<Object[]> obtenerVentasPorHoraRaw(
            @Param("idSucursal") Long idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
    /**
     * Ventas por categoría
     */
    @Query(value = "SELECT COALESCE(cat.nombre_categoria, 'Sin Categoría') as categoria, " +
           "COALESCE(SUM(dv.cantidad), 0) as cantidad " +
           "FROM venta v " +
           "JOIN detalle_venta dv ON v.id_venta = dv.id_venta " +
           "JOIN variante var ON dv.id_variante = var.id_variante " +
           "JOIN modelo_color mc ON var.id_modelo_color = mc.id_modelo_color " +
           "JOIN modelo m ON mc.id_modelo = m.id_modelo " +
           "LEFT JOIN categoria cat ON m.id_categoria = cat.id_categoria " +
           "WHERE v.estado_venta = true " +
           "AND (:idSucursal IS NULL OR v.id_sucursal = :idSucursal) " +
           "AND v.fecha_venta BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY cat.nombre_categoria " +
           "ORDER BY cantidad DESC",
           nativeQuery = true)
    List<Object[]> obtenerVentasPorCategoriaRaw(
            @Param("idSucursal") Long idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
    /**
     * Métodos de pago
     */
    @Query(value = "SELECT 'Efectivo' as metodo, COALESCE(SUM(monto_efectivo), 0) as total " +
           "FROM venta " +
           "WHERE estado_venta = true " +
           "AND (:idSucursal IS NULL OR id_sucursal = :idSucursal) " +
           "AND fecha_venta BETWEEN :fechaInicio AND :fechaFin " +
           "UNION ALL " +
           "SELECT 'QR' as metodo, COALESCE(SUM(monto_qr), 0) as total " +
           "FROM venta " +
           "WHERE estado_venta = true " +
           "AND (:idSucursal IS NULL OR id_sucursal = :idSucursal) " +
           "AND fecha_venta BETWEEN :fechaInicio AND :fechaFin " +
           "UNION ALL " +
           "SELECT 'Tarjeta' as metodo, COALESCE(SUM(monto_tarjeta), 0) as total " +
           "FROM venta " +
           "WHERE estado_venta = true " +
           "AND (:idSucursal IS NULL OR id_sucursal = :idSucursal) " +
           "AND fecha_venta BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY total DESC",
           nativeQuery = true)
    List<Object[]> obtenerMetodosPagoRaw(
            @Param("idSucursal") Long idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Distribución de tallas
     */
    @Query(value = "SELECT COALESCE(t.nombre_talla, 'Sin Talla') as talla, " +
           "COALESCE(SUM(dv.cantidad), 0) as cantidad " +
           "FROM venta v " +
           "JOIN detalle_venta dv ON v.id_venta = dv.id_venta " +
           "JOIN variante var ON dv.id_variante = var.id_variante " +
           "LEFT JOIN talla t ON var.id_talla = t.id_talla " +
           "WHERE v.estado_venta = true " +
           "AND (:idSucursal IS NULL OR v.id_sucursal = :idSucursal) " +
           "AND v.fecha_venta BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY t.nombre_talla " +
           "ORDER BY cantidad DESC",
           nativeQuery = true)
    List<Object[]> obtenerDistribucionTallasRaw(
            @Param("idSucursal") Long idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Top productos más vendidos
     */
    @Query(value = "SELECT " +
           "m.nombre_modelo as nombreModelo, " +
           "CONCAT(COALESCE(cat.nombre_categoria, 'Sin Categoría'), ' - ', COALESCE(mar.nombre_marca, 'Sin Marca')) as subtitulo, " +
           "SUM(dv.cantidad) as cantidadVendida, " +
           "(SELECT mc2.foto_url FROM modelo_color mc2 WHERE mc2.id_modelo = m.id_modelo LIMIT 1) as fotoUrl, " +
           "m.id_modelo as idModelo " +
           "FROM venta v " +
           "JOIN detalle_venta dv ON v.id_venta = dv.id_venta " +
           "JOIN variante var ON dv.id_variante = var.id_variante " +
           "JOIN modelo_color mc ON var.id_modelo_color = mc.id_modelo_color " +
           "JOIN modelo m ON mc.id_modelo = m.id_modelo " +
           "LEFT JOIN categoria cat ON m.id_categoria = cat.id_categoria " +
           "LEFT JOIN marca mar ON m.id_marca = mar.id_marca " +
           "WHERE v.estado_venta = true " +
           "AND (:idSucursal IS NULL OR v.id_sucursal = :idSucursal) " +
           "AND v.fecha_venta BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY m.id_modelo, m.nombre_modelo, cat.nombre_categoria, mar.nombre_marca " +
           "ORDER BY cantidadVendida DESC " +
           "LIMIT :limit",
           nativeQuery = true)
    List<Object[]> obtenerTopProductosRaw(
            @Param("idSucursal") Long idSucursal,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("limit") int limit
    );

    /**
     * Obtener stock actual de un modelo (suma de todas las variantes)
     */
    @Query(value = "SELECT COALESCE(SUM(i.stock_inventario), 0) " +
           "FROM inventario i " +
           "JOIN variante var ON i.id_variante = var.id_variante " +
           "JOIN modelo_color mc ON var.id_modelo_color = mc.id_modelo_color " +
           "WHERE mc.id_modelo = :idModelo " +
           "AND i.estado_inventario = true " +
           "AND (:idSucursal IS NULL OR i.id_sucursal = :idSucursal)",
           nativeQuery = true)
    Long obtenerStockActualPorModelo(
            @Param("idModelo") Integer idModelo,
            @Param("idSucursal") Long idSucursal
    );
}
