package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence;

import com.sistemasTarija.dunno.inventario.application.dto.InventarioResumenDTO;
import com.sistemasTarija.dunno.inventario.application.port.out.InventarioPersistencePort;
import com.sistemasTarija.dunno.inventario.domain.model.*;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.InventarioEntity;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.SucursalEntity;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo.*;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.mapper.*;
import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InventarioRepositoryAdapter implements InventarioPersistencePort {
    
    private final InventarioRepository inventarioRepository;
    private final ModeloInventarioRepository modeloRepository;
    private final ModeloColorInventarioRepository modeloColorRepository;
    private final VarianteInventarioRepository varianteRepository;
    private final SucursalRepository sucursalRepository;
    
    private final InventarioPersistenceMapper inventarioMapper;
    private final ModeloPersistenceMapper modeloMapper;
    private final SucursalPersistenceMapper sucursalMapper;
    
    @Override
    public List<InventarioResumenDTO> obtenerResumenGlobal() {
        // Query optimizada ejecutada directamente en BD
        return inventarioRepository.obtenerResumenGlobal();
    }
    
    @Override
    public Optional<Modelo> findModeloById(Integer idModelo) {
        Optional<ModeloInventarioEntity> modeloEntity = modeloRepository.findByIdWithRelations(idModelo);
        
        if (modeloEntity.isEmpty()) {
            return Optional.empty();
        }
        
        // Cargar colores con sus variantes
        List<ModeloColorInventarioEntity> coloresEntities = modeloColorRepository.findByModeloIdWithColor(idModelo);
        
        // Para cada color, cargar sus variantes
        for (ModeloColorInventarioEntity colorEntity : coloresEntities) {
            List<VarianteInventarioEntity> variantesEntities = 
                    varianteRepository.findByModeloColorIdWithTalla(colorEntity.getId());
            // No necesitamos setear las variantes en la entity, pero las mapeamos después
        }
        
        // Mapear a dominio
        Modelo modelo = modeloMapper.toDomain(modeloEntity.get(), coloresEntities);
        
        // Mapear variantes para cada color
        for (int i = 0; i < coloresEntities.size(); i++) {
            ModeloColorInventarioEntity colorEntity = coloresEntities.get(i);
            List<VarianteInventarioEntity> variantesEntities = 
                    varianteRepository.findByModeloColorIdWithTalla(colorEntity.getId());
            
            List<Variante> variantes = variantesEntities.stream()
                    .map(modeloMapper::varianteToDomain)
                    .collect(Collectors.toList());
            
            modelo.getColores().get(i).setVariantes(variantes);
        }
        
        return Optional.of(modelo);
    }
    
    @Override
    public List<Inventario> findInventariosByModelo(Integer idModelo) {
        List<InventarioEntity> entities = inventarioRepository.findInventariosByModelo(idModelo);
        
        return entities.stream()
                .map(inventarioMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Sucursal> findAllSucursales() {
        List<SucursalEntity> entities = sucursalRepository.findAllByEstadoTrue();
        
        return entities.stream()
                .map(sucursalMapper::toDomain)
                .collect(Collectors.toList());
    }
}
