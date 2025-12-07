package com.sistemasTarija.dunno.venta.application.port.out;

import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto.InventarioRawDTO;

import java.util.List;

public interface InventarioCatalogoVentaPersistancePort {

    List<InventarioRawDTO> obtenerDetalleModeloRaw(Integer idSucursal, Integer idModelo);
    List<ResumenPrendaDTO> obtenerListadoResumen(Integer idSucursal);
}
