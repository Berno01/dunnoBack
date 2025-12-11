package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence;

import com.sistemasTarija.dunno.catalogo.application.port.out.CatalogoPersistencePort;
import com.sistemasTarija.dunno.catalogo.domain.model.options.*;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.*;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.mapper.CatalogoPersistenceMapper;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de Persistencia: implementa el puerto de salida usando repositorios JPA
 */
@Component
@RequiredArgsConstructor
public class CatalogoRepositoryAdapter implements CatalogoPersistencePort {

    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    private final CorteRepository corteRepository;
    private final TallaRepository tallaRepository;
    private final ColorRepository colorRepository;
    private final CatalogoPersistenceMapper mapper;

    // ==================== MARCA ====================
    @Override
    public Marca saveMarca(Marca marca) {
        MarcaCatalogoEntity entity = mapper.toMarcaEntity(marca);
        MarcaCatalogoEntity savedEntity = marcaRepository.save(entity);
        return mapper.toMarcaDomain(savedEntity);
    }

    @Override
    public Optional<Marca> findMarcaById(Integer id) {
        return marcaRepository.findById(id)
                .map(mapper::toMarcaDomain);
    }

    @Override
    public Optional<Marca> findMarcaByNombre(String nombre) {
        return marcaRepository.findByNombre(nombre)
                .map(mapper::toMarcaDomain);
    }

    @Override
    public List<Marca> findAllMarcas() {
        return mapper.toMarcaDomainList(marcaRepository.findByEstadoTrue());
    }

    // ==================== CATEGORIA ====================
    @Override
    public Categoria saveCategoria(Categoria categoria) {
        CategoriaCatalogoEntity entity = mapper.toCategoriaEntity(categoria);
        CategoriaCatalogoEntity savedEntity = categoriaRepository.save(entity);
        return mapper.toCategoriaDomain(savedEntity);
    }

    @Override
    public Optional<Categoria> findCategoriaById(Integer id) {
        return categoriaRepository.findById(id)
                .map(mapper::toCategoriaDomain);
    }

    @Override
    public Optional<Categoria> findCategoriaByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .map(mapper::toCategoriaDomain);
    }

    @Override
    public List<Categoria> findAllCategorias() {
        return mapper.toCategoriaDomainList(categoriaRepository.findByEstadoTrue());
    }

    // ==================== CORTE ====================
    @Override
    public Corte saveCorte(Corte corte) {
        CorteCatalogoEntity entity = mapper.toCorteEntity(corte);
        CorteCatalogoEntity savedEntity = corteRepository.save(entity);
        return mapper.toCorteDomain(savedEntity);
    }

    @Override
    public Optional<Corte> findCorteById(Integer id) {
        return corteRepository.findById(id)
                .map(mapper::toCorteDomain);
    }

    @Override
    public Optional<Corte> findCorteByNombre(String nombre) {
        return corteRepository.findByNombre(nombre)
                .map(mapper::toCorteDomain);
    }

    @Override
    public List<Corte> findAllCortes() {
        return mapper.toCorteDomainList(corteRepository.findByEstadoTrue());
    }

    // ==================== TALLA ====================
    @Override
    public Talla saveTalla(Talla talla) {
        TallaCatalogoEntity entity = mapper.toTallaEntity(talla);
        TallaCatalogoEntity savedEntity = tallaRepository.save(entity);
        return mapper.toTallaDomain(savedEntity);
    }

    @Override
    public Optional<Talla> findTallaById(Integer id) {
        return tallaRepository.findById(id)
                .map(mapper::toTallaDomain);
    }

    @Override
    public Optional<Talla> findTallaByNombre(String nombre) {
        return tallaRepository.findByNombre(nombre)
                .map(mapper::toTallaDomain);
    }

    @Override
    public List<Talla> findAllTallas() {
        return mapper.toTallaDomainList(tallaRepository.findByEstadoTrue());
    }

    // ==================== COLOR ====================
    @Override
    public Color saveColor(Color color) {
        ColorCatalogoEntity entity = mapper.toColorEntity(color);
        ColorCatalogoEntity savedEntity = colorRepository.save(entity);
        return mapper.toColorDomain(savedEntity);
    }

    @Override
    public Optional<Color> findColorById(Integer id) {
        return colorRepository.findById(id)
                .map(mapper::toColorDomain);
    }

    @Override
    public Optional<Color> findColorByNombre(String nombre) {
        return colorRepository.findByNombre(nombre)
                .map(mapper::toColorDomain);
    }

    @Override
    public List<Color> findAllColores() {
        return mapper.toColorDomainList(colorRepository.findByEstadoTrue());
    }
}
