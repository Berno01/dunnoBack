package com.sistemasTarija.dunno.recepcion.application.port.in;

import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.DetalleModeloDTO;
import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ResumenModeloDTO;

import java.util.List;

public interface FindModeloCatalogoUseCase {
    
    /**
     * Obtiene el listado completo de modelos activos del catálogo.
     * NO filtra por inventario ni sucursal.
     */
    List<ResumenModeloDTO> getListadoModelos();
    
    /**
     * Obtiene el detalle completo de un modelo específico con todos sus colores y variantes.
     * NO filtra por inventario.
     */
    DetalleModeloDTO getDetalleModelo(Integer idModelo);
}
