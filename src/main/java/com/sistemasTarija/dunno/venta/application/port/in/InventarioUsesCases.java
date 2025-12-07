package com.sistemasTarija.dunno.venta.application.port.in;

import com.sistemasTarija.dunno.venta.domain.model.Inventario;

import java.util.List;
import java.util.Optional;

public interface InventarioUsesCases {
    List<Inventario> findAll();
    Optional<Inventario> findById(Integer idInventario);
}
