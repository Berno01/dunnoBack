package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.in.web;

import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.DetalleModeloDTO;
import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ResumenModeloDTO;
import com.sistemasTarija.dunno.recepcion.application.port.in.FindModeloCatalogoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drops/catalogo")
@RequiredArgsConstructor
public class ModeloCatalogoController {
    
    private final FindModeloCatalogoUseCase findModeloCatalogoUseCase;

    /**
     * GET /api/drops/catalogo
     * Obtiene el listado completo de modelos activos.
     * NO requiere idSucursal porque muestra TODOS los modelos.
     */
    @GetMapping
    public ResponseEntity<List<ResumenModeloDTO>> obtenerListadoModelos() {
        return ResponseEntity.ok(findModeloCatalogoUseCase.getListadoModelos());
    }

    /**
     * GET /api/drops/catalogo/{idModelo}
     * Obtiene el detalle completo de un modelo con todos sus colores y variantes.
     * NO requiere idSucursal porque muestra todas las variantes sin filtrar por stock.
     */
    @GetMapping("/{idModelo}")
    public ResponseEntity<DetalleModeloDTO> obtenerDetalleModelo(@PathVariable Integer idModelo) {
        return ResponseEntity.ok(findModeloCatalogoUseCase.getDetalleModelo(idModelo));
    }
}
