package com.sistemasTarija.dunno.inventario.infrastructure.adapter.in.web;

import com.sistemasTarija.dunno.inventario.application.dto.InventarioDetalleDTO;
import com.sistemasTarija.dunno.inventario.application.dto.InventarioResumenDTO;
import com.sistemasTarija.dunno.inventario.application.port.in.ConsultarInventarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el módulo de Inventario
 * Endpoints para consulta y visualización estratégica del inventario
 */
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {
    
    private final ConsultarInventarioUseCase consultarInventarioUseCase;
    
    /**
     * Endpoint 1: Listado General de Inventario
     * GET /api/inventario
     * 
     * Retorna todos los modelos con su stock total global
     * (suma de todas las variantes en todas las sucursales)
     * 
     * @return Lista de resúmenes de inventario
     */
    @GetMapping
    public ResponseEntity<List<InventarioResumenDTO>> obtenerListadoGeneral() {
        List<InventarioResumenDTO> inventarios = consultarInventarioUseCase.obtenerListadoGeneral();
        return ResponseEntity.ok(inventarios);
    }
    
    /**
     * Endpoint 2: Detalle Matricial del Modelo
     * GET /api/inventario/{idModelo}
     * 
     * Retorna la matriz completa Color x Talla del modelo
     * con stocks por sucursal (incluyendo "Global")
     * 
     * @param idModelo ID del modelo a consultar
     * @return Detalle completo con matriz de stocks
     */
    @GetMapping("/{idModelo}")
    public ResponseEntity<InventarioDetalleDTO> obtenerDetalleModelo(
            @PathVariable Integer idModelo) {
        InventarioDetalleDTO detalle = consultarInventarioUseCase.obtenerDetalleModelo(idModelo);
        return ResponseEntity.ok(detalle);
    }
}
