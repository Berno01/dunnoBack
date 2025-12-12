package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemasTarija.dunno.recepcion.application.dto.RecepcionDTO;
import com.sistemasTarija.dunno.recepcion.application.dto.RecepcionFilterDTO;
import com.sistemasTarija.dunno.recepcion.application.port.in.CreateRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.in.DeleteRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.in.FindRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.in.UpdateRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drops")
@RequiredArgsConstructor
public class RecepcionController {

    private final CreateRecepcionUseCase createRecepcionUseCase;
    private final FindRecepcionUseCase findRecepcionUseCase;
    private final UpdateRecepcionUseCase updateRecepcionUseCase;
    private final DeleteRecepcionUseCase deleteRecepcionUseCase;

    @PostMapping
    public ResponseEntity<Recepcion> crearRecepcion(
            @RequestBody RecepcionDTO recepcionDTO,
            @RequestHeader("idUsuario") Integer idUsuario) {
        Recepcion nuevaRecepcion = createRecepcionUseCase.save(recepcionDTO);
        return new ResponseEntity<>(nuevaRecepcion, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecepcionDTO> obtenerRecepcion(
            @PathVariable Integer id,
            @RequestHeader("idUsuario") Integer idUsuario) {
        Optional<RecepcionDTO> recepcion = findRecepcionUseCase.findById(id, idUsuario);
        return recepcion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<RecepcionDTO>> listarRecepciones(
            @RequestHeader("idUsuario") Integer idUsuario,
            @RequestParam(required = false) Integer idSucursal,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "fecha_fin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        RecepcionFilterDTO filtro = new RecepcionFilterDTO(idSucursal, fecha, fechaFin);
        List<RecepcionDTO> recepciones = findRecepcionUseCase.findAll(filtro, idUsuario);
        return ResponseEntity.ok(recepciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recepcion> actualizarRecepcion(
            @PathVariable Integer id,
            @RequestBody RecepcionDTO recepcionDTO,
            @RequestHeader("idUsuario") Integer idUsuario) {
        Recepcion recepcionActualizada = updateRecepcionUseCase.update(id, recepcionDTO, idUsuario);
        return ResponseEntity.ok(recepcionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> anularRecepcion(
            @PathVariable Integer id,
            @RequestHeader("idUsuario") Integer idUsuario) {
        deleteRecepcionUseCase.anularRecepcion(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}
