package com.sistemasTarija.dunno.inventario.application.port.in;

import com.sistemasTarija.dunno.inventario.application.dto.InventarioDetalleDTO;
import com.sistemasTarija.dunno.inventario.application.dto.InventarioResumenDTO;

import java.util.List;

/**
 * Puerto de entrada para consultas de inventario
 */
public interface ConsultarInventarioUseCase {
    
    /**
     * Obtiene el listado general de todos los modelos con su stock total global
     * @return Lista de resúmenes de inventario
     */
    List<InventarioResumenDTO> obtenerListadoGeneral();
    
    /**
     * Obtiene el detalle completo de un modelo con matriz Color x Talla por sucursal
     * @param idModelo ID del modelo a consultar
     * @return Detalle completo del inventario del modelo
     */
    InventarioDetalleDTO obtenerDetalleModelo(Integer idModelo);
}
