package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository;

import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;
import com.sistemasTarija.dunno.venta.domain.model.Inventario;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto.InventarioRawDTO;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.InventarioVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventarioVentaRepository extends JpaRepository<InventarioVentaEntity, Integer> {
    List<InventarioVentaEntity> findAllByEstadoTrue();
    @Query("SELECT i FROM InventarioVentaEntity i WHERE i.variante.id = :idVariante AND i.idSucursal = :idSucursal")
    Optional<InventarioVentaEntity> findByIdVarianteAndIdSucursal(
            @Param("idVariante") Integer idVariante,
            @Param("idSucursal") Integer idSucursal
    );



    @Query("SELECT new com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO(" +
            "m.id, m.nombre, m.precio, ma.nombre, cat.nombre, " +
            "(SELECT MIN(mc.fotoUrl) FROM ModeloColorEntity mc WHERE mc.modelo.id = m.id), " +
            "SUM(i.stockInventario), " +
            "CASE WHEN SUM(i.stockInventario) < 5 THEN true ELSE false END) " +
            "FROM InventarioVentaEntity i " +
            "JOIN i.variante v " +
            "JOIN v.modeloColor mc " +
            "JOIN mc.modelo m " +
            "JOIN m.marca ma " +
            "JOIN m.categoria cat " +
            "WHERE i.idSucursal = :idSucursal " +
            "AND i.estado = true " +
            "AND m.estado = true " +
            "GROUP BY m.id, m.nombre, m.precio, ma.nombre, cat.nombre")
    List<ResumenPrendaDTO> obtenerListadoResumen(@Param("idSucursal") Integer idSucursal);

    @Query("SELECT new com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto.InventarioRawDTO(" +
            "m.id, " +                  // idModelo
            "m.nombre, " +              // nombreModelo
            "m.precio, " +              // precio
            "ma.nombre, " +             // nombreMarca
            "cat.nombre, " +            // nombreCategoria
            "co.nombre, " +             // nombreCorte
            "col.nombre, " +            // nombreColor
            "col.codigoHex, " +         // codigoHex
            "mc.fotoUrl, " +            // fotoUrl
            "v.id, " +                  // idVariante (OJO: v.id viene de VarianteEntity)
            "t.nombre, " +              // nombreTalla
            "i.stockInventario) " +     // stock
            "FROM InventarioVentaEntity i " +
            "JOIN i.variante v " +
            "JOIN v.modeloColor mc " +
            "JOIN mc.modelo m " +
            "JOIN m.marca ma " +
            "JOIN m.categoria cat " +
            "JOIN m.corte co " +
            "JOIN mc.color col " +
            "JOIN v.talla t " +
            "WHERE i.idSucursal = :idSucursal " +
            "AND m.id = :idModelo " +
            "AND m.estado = true " +
            "AND i.estado = true " +
            "ORDER BY col.nombre, t.nombre")
    List<InventarioRawDTO> obtenerDetalleModeloRaw(@Param("idSucursal") Integer idSucursal, @Param("idModelo") Integer idModelo);
}


