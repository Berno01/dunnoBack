package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence;

import com.sistemasTarija.dunno.catalogo.application.port.out.ModeloPersistencePort;
import com.sistemasTarija.dunno.catalogo.domain.model.Modelo;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.mapper.ModeloPersistenceMapper;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository.ModeloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModeloRepositoryAdapter implements ModeloPersistencePort {

    private final ModeloRepository modeloRepository;
    private final ModeloPersistenceMapper modeloPersistenceMapper;

    @Override
    public Modelo saveModelo(Modelo modelo) {
        ModeloCatalogoEntity entity = modeloPersistenceMapper.toEntity(modelo);
        
        // Asegurar que la relación bidireccional esté correctamente establecida antes de guardar
        if (entity.getColores() != null) {
            entity.getColores().forEach(color -> {
                color.setModelo(entity);
                if (color.getVariantes() != null) {
                    color.getVariantes().forEach(variante -> variante.setModeloColor(color));
                }
            });
        }
        
        ModeloCatalogoEntity savedEntity = modeloRepository.save(entity);
        return modeloPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Modelo> findById(Integer id) {
        return modeloRepository.findById(id)
                .map(modeloPersistenceMapper::toDomain);
    }

    @Override
    public List<Modelo> findAll() {
        return modeloRepository.findAll().stream()
                .map(modeloPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return modeloRepository.existsByNombre(nombre);
    }
}
