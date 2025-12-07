package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.repository;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloCatalogoEntity;
import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ResumenModeloDTO;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.dto.ModeloRawDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModeloCatalogoRepository extends JpaRepository<ModeloCatalogoEntity, Integer> {

    /**
     * Obtiene un listado resumido de todos los modelos activos.
     * Sin filtrar por stock ni sucursal.
     */
    @Query("SELECT new com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ResumenModeloDTO(" +
            "m.id, m.nombre, ma.nombre, cat.nombre, " +
            "(SELECT MIN(mc.fotoUrl) FROM ModeloColorCatalogoEntity mc WHERE mc.modelo.id = m.id)) " +
            "FROM ModeloCatalogoEntity m " +
            "JOIN m.marca ma " +
            "JOIN m.categoria cat " +
            "WHERE m.estado = true " +
            "ORDER BY m.nombre")
    List<ResumenModeloDTO> obtenerListadoModelos();

    /**
     * Obtiene el detalle completo de un modelo con todos sus colores y variantes.
     * Sin filtrar por inventario.
     */
    @Query("SELECT new com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.dto.ModeloRawDTO(" +
            "m.id, " +                  // idModelo
            "m.nombre, " +              // nombreModelo
            "ma.nombre, " +             // nombreMarca
            "cat.nombre, " +            // nombreCategoria
            "co.nombre, " +             // nombreCorte
            "col.nombre, " +            // nombreColor
            "col.codigoHex, " +         // codigoHex
            "mc.fotoUrl, " +            // fotoUrl
            "v.id, " +                  // idVariante
            "t.nombre) " +              // nombreTalla
            "FROM ModeloCatalogoEntity m " +
            "JOIN m.marca ma " +
            "JOIN m.categoria cat " +
            "JOIN m.corte co " +
            "JOIN m.colores mc " +
            "JOIN mc.color col " +
            "JOIN mc.variantes v " +
            "JOIN v.talla t " +
            "WHERE m.id = :idModelo " +
            "AND m.estado = true " +
            "ORDER BY col.nombre, t.nombre")
    List<ModeloRawDTO> obtenerDetalleModeloRaw(@Param("idModelo") Integer idModelo);
}
