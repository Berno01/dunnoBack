package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence;

import com.sistemasTarija.dunno.catalogo.application.port.out.ModeloPersistencePort;
import com.sistemasTarija.dunno.catalogo.domain.model.Modelo;
import com.sistemasTarija.dunno.catalogo.domain.model.ModeloColor;
import com.sistemasTarija.dunno.catalogo.domain.model.Variante;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.ModeloColorCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.VarianteCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.mapper.ModeloPersistenceMapper;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository.ModeloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModeloRepositoryAdapter implements ModeloPersistencePort {

    private final ModeloRepository modeloRepository;
    private final ModeloPersistenceMapper modeloPersistenceMapper;

    @Override
    public Modelo saveModelo(Modelo modelo) {
        // Si es una actualización (tiene ID), cargar la entidad existente
        if (modelo.getId() != null) {
            Optional<ModeloCatalogoEntity> existingEntityOpt = modeloRepository.findById(modelo.getId());
            
            if (existingEntityOpt.isPresent()) {
                ModeloCatalogoEntity existingEntity = existingEntityOpt.get();
                
                // Actualizar campos básicos del modelo usando los IDs directamente
                existingEntity.setNombre(modelo.getNombre());
                existingEntity.setIdMarca(modelo.getIdMarca());
                existingEntity.setIdCategoria(modelo.getIdCategoria());
                existingEntity.setIdCorte(modelo.getIdCorte());
                existingEntity.setPrecio(modelo.getPrecio());
                existingEntity.setEstado(modelo.getEstado());
                
                // Manejar colores y variantes de forma inteligente (sin eliminar, solo actualizar/agregar)
                updateColoresYVariantes(existingEntity, modelo);
                
                ModeloCatalogoEntity savedEntity = modeloRepository.save(existingEntity);
                return modeloPersistenceMapper.toDomain(savedEntity);
            }
        }
        
        // Si es nuevo modelo (sin ID)
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
    
    private void updateColoresYVariantes(ModeloCatalogoEntity existingEntity, Modelo modelo) {
        // Crear un mapa de colores existentes por idColor para acceso rápido
        Map<Integer, ModeloColorCatalogoEntity> existingColorMap = existingEntity.getColores().stream()
                .collect(Collectors.toMap(
                        ModeloColorCatalogoEntity::getIdColor,
                        color -> color,
                        (c1, c2) -> c1  // En caso de duplicados (no debería pasar)
                ));
        
        // Procesar cada color del request
        for (ModeloColor modeloColor : modelo.getColores()) {
            ModeloColorCatalogoEntity existingColor = existingColorMap.get(modeloColor.getIdColor());
            
            if (existingColor != null) {
                // ✅ El color YA EXISTE: solo MODIFICAR la foto y actualizar variantes
                existingColor.setFotoUrl(modeloColor.getFotoUrl());
                updateVariantes(existingColor, modeloColor);
            } else {
                // ✅ Color NUEVO: AGREGAR a la lista existente
                ModeloColorCatalogoEntity newColor = new ModeloColorCatalogoEntity();
                newColor.setFotoUrl(modeloColor.getFotoUrl());
                newColor.setIdColor(modeloColor.getIdColor());
                newColor.setModelo(existingEntity);
                newColor.setVariantes(new ArrayList<>());
                
                // Agregar las variantes del color nuevo
                if (modeloColor.getVariantes() != null) {
                    for (Variante variante : modeloColor.getVariantes()) {
                        VarianteCatalogoEntity varianteEntity = new VarianteCatalogoEntity();
                        varianteEntity.setIdTalla(variante.getIdTalla());
                        varianteEntity.setModeloColor(newColor);
                        newColor.getVariantes().add(varianteEntity);
                    }
                }
                
                // ⚠️ IMPORTANTE: agregamos a la lista SIN hacer clear()
                existingEntity.getColores().add(newColor);
            }
        }
        
        // ⚠️ NO eliminamos colores que no vienen en el request
        // Esto evita problemas de FK y mantiene el historial
    }
    
    private void updateVariantes(ModeloColorCatalogoEntity existingColor, ModeloColor modeloColor) {
        // Crear set de tallas existentes para búsqueda rápida
        Set<Integer> existingTallas = existingColor.getVariantes().stream()
                .map(VarianteCatalogoEntity::getIdTalla)
                .collect(Collectors.toSet());
        
        // Procesar cada variante del request
        if (modeloColor.getVariantes() != null) {
            for (Variante variante : modeloColor.getVariantes()) {
                if (!existingTallas.contains(variante.getIdTalla())) {
                    // ✅ Variante NUEVA: AGREGAR a la lista existente
                    VarianteCatalogoEntity newVariante = new VarianteCatalogoEntity();
                    newVariante.setIdTalla(variante.getIdTalla());
                    newVariante.setModeloColor(existingColor);
                    existingColor.getVariantes().add(newVariante);
                }
                // ✅ Si la variante YA EXISTE: NO hacer nada (no hay campos que modificar)
                // Las variantes solo tienen idTalla, que es el identificador
            }
        }
        
        // ⚠️ NO eliminamos variantes que no vienen en el request
        // Esto evita problemas de FK con detalle_recepcion, inventario, etc.
    }

    @Override
    public Optional<Modelo> findById(Integer id) {
        return modeloRepository.findById(id)
                .map(modeloPersistenceMapper::toDomain);
    }

    @Override
    public List<Modelo> findAll() {
        return modeloRepository.findByEstadoTrue().stream()
                .map(modeloPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return modeloRepository.existsByNombre(nombre);
    }
}
