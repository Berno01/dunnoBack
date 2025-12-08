package com.sistemasTarija.dunno.inventario.application.port.out;

import com.sistemasTarija.dunno.inventario.application.dto.InventarioResumenDTO;
import com.sistemasTarija.dunno.inventario.domain.model.Inventario;
import com.sistemasTarija.dunno.inventario.domain.model.Modelo;
import com.sistemasTarija.dunno.inventario.domain.model.Sucursal;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de inventario
 */
public interface InventarioPersistencePort {
    
    /**
     * Obtiene el listado resumido de inventario con stock global por modelo
     * Optimizado con query directa en BD
     * @return Lista de resúmenes de inventario
     */
    List<InventarioResumenDTO> obtenerResumenGlobal();
    
    /**
     * Busca un modelo por su ID con todos sus datos relacionados
     * @param idModelo ID del modelo
     * @return Optional con el modelo si existe
     */
    Optional<Modelo> findModeloById(Integer idModelo);
    
    /**
     * Obtiene todos los registros de inventario de un modelo específico
     * @param idModelo ID del modelo
     * @return Lista de registros de inventario
     */
    List<Inventario> findInventariosByModelo(Integer idModelo);
    
    /**
     * Obtiene todas las sucursales activas
     * @return Lista de sucursales
     */
    List<Sucursal> findAllSucursales();
}
