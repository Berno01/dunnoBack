package com.sistemasTarija.dunno.venta.application.port.out;

import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;
import com.sistemasTarija.dunno.venta.domain.model.Inventario;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto.InventarioRawDTO;

import java.util.List;
import java.util.Optional;

public interface InventarioPersistancePort {
    Optional<Inventario> findById(Integer idInventario);
    Inventario save(Inventario idInventario);
    List<Inventario> findAll();
    Optional<Inventario> findByIdVarianteAndIdSucursal(Integer idVariante, Integer idSucursal);




}
