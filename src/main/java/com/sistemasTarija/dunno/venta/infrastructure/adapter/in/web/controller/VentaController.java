package com.sistemasTarija.dunno.venta.infrastructure.adapter.in.web.controller;

import com.sistemasTarija.dunno.venta.application.dto.VentaDTO;
import com.sistemasTarija.dunno.venta.application.dto.VentaFilterDTO;
import com.sistemasTarija.dunno.venta.application.port.in.CreateVentaUseCase;
import com.sistemasTarija.dunno.venta.application.port.in.DeleteVentaUseCase;
import com.sistemasTarija.dunno.venta.application.port.in.FindVentaUseCase;
import com.sistemasTarija.dunno.venta.application.port.in.UpdateVentaUseCase;
import com.sistemasTarija.dunno.venta.domain.model.Venta;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/venta")
public class VentaController {
    private final CreateVentaUseCase createVentaUseCase;
    private final FindVentaUseCase findVentaUseCase;
    private final UpdateVentaUseCase updateVentaUseCase;
    private final DeleteVentaUseCase deleteVentaUseCase;

    public VentaController(CreateVentaUseCase createVentaUseCase, FindVentaUseCase findVentaUseCase, UpdateVentaUseCase updateVentaUseCase,  DeleteVentaUseCase deleteVentaUseCase) {
        this.createVentaUseCase = createVentaUseCase;
        this.findVentaUseCase  = findVentaUseCase;
        this.updateVentaUseCase  = updateVentaUseCase;
        this.deleteVentaUseCase = deleteVentaUseCase;

    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createVenta(@RequestBody VentaDTO ventaDTO) {

        createVentaUseCase.save(ventaDTO);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Venta registrada exitosamente");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{idVenta}")
    public ResponseEntity<VentaDTO> updateVenta(
            @PathVariable Integer idVenta,
            @RequestBody VentaDTO ventaDTO,
            @RequestHeader("X-Usuario-Id") Integer idUsuario
    ) {
        VentaDTO ventaActualizada = updateVentaUseCase.update(idVenta, ventaDTO, idUsuario);
        return ResponseEntity.ok(ventaActualizada);
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> findAll(
            @RequestHeader("X-Usuario-Id") Integer idUsuario,
            @RequestParam(required = false) Integer idSucursal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        VentaFilterDTO filtro = new VentaFilterDTO(idSucursal, fecha);
        return ResponseEntity.ok(findVentaUseCase.findAll(filtro, idUsuario));
    }

    @GetMapping("/{idVenta}")
    public ResponseEntity<VentaDTO> findById(
            @RequestHeader("X-Usuario-Id") Integer idUsuario,
            @PathVariable Integer idVenta
    ) {
        return findVentaUseCase.findById(idVenta, idUsuario)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{idVenta}")
    public ResponseEntity<Void> deleteVenta(
            @PathVariable Integer idVenta,
            @RequestHeader("X-Usuario-Id") Integer idUsuario
    ) {
        deleteVentaUseCase.delete(idVenta, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idVenta}/activar")
    public ResponseEntity<Map<String, String>> activarVenta(
            @PathVariable Integer idVenta,
            @RequestHeader("X-Usuario-Id") Integer idUsuario
    ) {
        deleteVentaUseCase.activate(idVenta, idUsuario);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Venta reactivada exitosamente. El stock ha sido descontado.");
        return ResponseEntity.ok(response);
    }

}
