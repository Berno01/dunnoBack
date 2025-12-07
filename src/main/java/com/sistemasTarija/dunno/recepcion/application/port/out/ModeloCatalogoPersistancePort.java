package com.sistemasTarija.dunno.recepcion.application.port.out;

import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ResumenModeloDTO;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.dto.ModeloRawDTO;

import java.util.List;

public interface ModeloCatalogoPersistancePort {
    
    /**
     * Obtiene un listado resumido de TODOS los modelos activos del catálogo,
     * sin filtrar por inventario ni sucursal.
     */
    List<ResumenModeloDTO> obtenerListadoModelos();
    
    /**
     * Obtiene el detalle completo de un modelo específico con todos sus colores y variantes,
     * sin filtrar por inventario.
     */
    List<ModeloRawDTO> obtenerDetalleModeloRaw(Integer idModelo);
}
