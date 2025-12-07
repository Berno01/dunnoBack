package com.sistemasTarija.dunno.catalogo.application.port.out;

import com.sistemasTarija.dunno.catalogo.domain.model.options.*;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia del catálogo
 */
public interface CatalogoPersistencePort {
    
    // === MARCA ===
    Marca saveMarca(Marca marca);
    Optional<Marca> findMarcaById(Integer id);
    Optional<Marca> findMarcaByNombre(String nombre);
    List<Marca> findAllMarcas();
    
    // === CATEGORIA ===
    Categoria saveCategoria(Categoria categoria);
    Optional<Categoria> findCategoriaById(Integer id);
    Optional<Categoria> findCategoriaByNombre(String nombre);
    List<Categoria> findAllCategorias();
    
    // === CORTE ===
    Corte saveCorte(Corte corte);
    Optional<Corte> findCorteById(Integer id);
    Optional<Corte> findCorteByNombre(String nombre);
    List<Corte> findAllCortes();
    
    // === TALLA ===
    Talla saveTalla(Talla talla);
    Optional<Talla> findTallaById(Integer id);
    Optional<Talla> findTallaByNombre(String nombre);
    List<Talla> findAllTallas();
    
    // === COLOR ===
    Color saveColor(Color color);
    Optional<Color> findColorById(Integer id);
    Optional<Color> findColorByNombre(String nombre);
    List<Color> findAllColores();
}
