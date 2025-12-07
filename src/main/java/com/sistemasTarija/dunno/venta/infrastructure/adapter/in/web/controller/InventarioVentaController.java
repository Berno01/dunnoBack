package com.sistemasTarija.dunno.venta.infrastructure.adapter.in.web.controller;


import com.sistemasTarija.dunno.venta.application.dto.catalogo.DetallePrendaDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;
import com.sistemasTarija.dunno.venta.application.port.in.FindCatalogoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioVentaController {
    private final FindCatalogoUseCase findCatalogoUseCase;

    @GetMapping("/catalogo")
    public ResponseEntity<List<ResumenPrendaDTO>> obtenerCatalogo(@RequestParam Integer idSucursal) {
        return ResponseEntity.ok(findCatalogoUseCase.getListadoGeneral(idSucursal));
    }


    @GetMapping("/catalogo/{idModelo}")
    public ResponseEntity<DetallePrendaDTO> obtenerDetalleModelo(
            @PathVariable Integer idModelo,
            @RequestParam Integer idSucursal
    ) {
        return ResponseEntity.ok(findCatalogoUseCase.getDetalleModelo(idModelo, idSucursal));
    }
}
