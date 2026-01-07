package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.in.web;

import com.sistemasTarija.dunno.catalogo.application.dto.ModeloDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.ModeloListadoDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.RegistrarModeloRequest;
import com.sistemasTarija.dunno.catalogo.application.port.in.ManageModeloUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo/modelo")
@RequiredArgsConstructor
public class ModeloController {

    private final ManageModeloUseCase manageModeloUseCase;

    @PostMapping
    public ResponseEntity<ModeloDTO> createModelo(@RequestBody RegistrarModeloRequest request) {
        ModeloDTO createdModelo = manageModeloUseCase.createModelo(request);
        return new ResponseEntity<>(createdModelo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeloDTO> updateModelo(@PathVariable Integer id, @RequestBody RegistrarModeloRequest request) {
        ModeloDTO updatedModelo = manageModeloUseCase.updateModelo(id, request);
        return ResponseEntity.ok(updatedModelo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloDTO> getModeloById(@PathVariable Integer id) {
        ModeloDTO modelo = manageModeloUseCase.findModeloById(id);
        return ResponseEntity.ok(modelo);
    }

    @GetMapping
    public ResponseEntity<List<ModeloListadoDTO>> getAllModelos() {
        List<ModeloListadoDTO> modelos = manageModeloUseCase.findAllModelosListado();
        return ResponseEntity.ok(modelos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelo(@PathVariable Integer id) {
        manageModeloUseCase.deleteModelo(id);
        return ResponseEntity.noContent().build();
    }
}
