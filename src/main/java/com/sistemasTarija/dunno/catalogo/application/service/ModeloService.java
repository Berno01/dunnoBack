package com.sistemasTarija.dunno.catalogo.application.service;

import com.sistemasTarija.dunno.catalogo.application.dto.ModeloDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.ModeloListadoDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.RegistrarModeloRequest;
import com.sistemasTarija.dunno.catalogo.application.mapper.ModeloMapper;
import com.sistemasTarija.dunno.catalogo.application.port.in.ManageModeloUseCase;
import com.sistemasTarija.dunno.catalogo.application.port.out.ModeloPersistencePort;
import com.sistemasTarija.dunno.catalogo.domain.exception.ModeloFailedException;
import com.sistemasTarija.dunno.catalogo.domain.model.Modelo;
import com.sistemasTarija.dunno.catalogo.domain.model.ModeloColor;
import com.sistemasTarija.dunno.catalogo.domain.model.Variante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModeloService implements ManageModeloUseCase {

    private final ModeloPersistencePort modeloPersistencePort;
    private final ModeloMapper modeloMapper;

    @Transactional
    @Override
    public ModeloDTO createModelo(RegistrarModeloRequest request) {

        // 1. Map Request to Domain
        Modelo modelo = modeloMapper.toDomain(request);
        modelo.setEstado(true);
        // 2. Generate Combinations (Business Logic)
        generateCombinations(modelo, request.getIdsTallas());

        // 3. Save
        Modelo savedModelo = modeloPersistencePort.saveModelo(modelo);

        // 4. Return DTO
        return modeloMapper.toDTO(savedModelo);
    }

    @Transactional
    @Override
    public ModeloDTO updateModelo(Integer id, RegistrarModeloRequest request) {
        // 1. Check existence
        Modelo existingModelo = modeloPersistencePort.findById(id)
                .orElseThrow(() -> new ModeloFailedException("Modelo no encontrado con id: " + id));

        // 2. Map Request to new Domain object
        Modelo modeloToUpdate = modeloMapper.toDomain(request);
        modeloToUpdate.setId(id);
        modeloToUpdate.setEstado(existingModelo.getEstado()); // Preserve state or update if needed? Usually update doesn't change logical delete state unless specified.

        // 3. Generate Combinations (Business Logic)
        generateCombinations(modeloToUpdate, request.getIdsTallas());

        // 4. Save (Update)
        Modelo savedModelo = modeloPersistencePort.saveModelo(modeloToUpdate);

        return modeloMapper.toDTO(savedModelo);
    }

    @Override
    public ModeloDTO findModeloById(Integer id) {
        return modeloPersistencePort.findById(id)
                .map(modeloMapper::toDTO)
                .orElseThrow(() -> new ModeloFailedException("Modelo no encontrado con id: " + id));
    }

    @Override
    public List<ModeloDTO> findAllModelos() {
        return modeloPersistencePort.findAll().stream()
                .map(modeloMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModeloListadoDTO> findAllModelosListado() {
        // Uses optimized persistence method (avoids variants loading) and optimized mapper (avoids variants serialization)
        return modeloPersistencePort.findAllListado().stream()
                .map(modeloMapper::toListadoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteModelo(Integer id) {
        Modelo modelo = modeloPersistencePort.findById(id)
                .orElseThrow(() -> new ModeloFailedException("Modelo no encontrado con id: " + id));
        
        modelo.setEstado(false);
        modeloPersistencePort.saveModelo(modelo);
    }

    private void generateCombinations(Modelo modelo, List<Integer> idsTallas) {
        if (modelo.getColores() != null && idsTallas != null) {
            for (ModeloColor modeloColor : modelo.getColores()) {
                List<Variante> variantes = new ArrayList<>();
                for (Integer idTalla : idsTallas) {
                    Variante variante = Variante.builder()
                            .idTalla(idTalla)
                            .build();
                    variantes.add(variante);
                }
                modeloColor.setVariantes(variantes);
            }
        }
    }
}
