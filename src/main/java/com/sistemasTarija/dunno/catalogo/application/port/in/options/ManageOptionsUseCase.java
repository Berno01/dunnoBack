package com.sistemasTarija.dunno.catalogo.application.port.in.options;

import com.sistemasTarija.dunno.catalogo.application.dto.options.ColorDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.options.OptionDTO;

import java.util.List;


public interface ManageOptionsUseCase {
    
    // === MARCA ===
    OptionDTO createMarca(OptionDTO dto);
    OptionDTO updateMarca(Integer id, OptionDTO dto);
    void deleteMarca(Integer id);
    OptionDTO findMarcaById(Integer id);
    List<OptionDTO> findAllMarcas();
    
    // === CATEGORIA ===
    OptionDTO createCategoria(OptionDTO dto);
    OptionDTO updateCategoria(Integer id, OptionDTO dto);
    void deleteCategoria(Integer id);
    OptionDTO findCategoriaById(Integer id);
    List<OptionDTO> findAllCategorias();
    
    // === CORTE ===
    OptionDTO createCorte(OptionDTO dto);
    OptionDTO updateCorte(Integer id, OptionDTO dto);
    void deleteCorte(Integer id);
    OptionDTO findCorteById(Integer id);
    List<OptionDTO> findAllCortes();
    
    // === TALLA ===
    OptionDTO createTalla(OptionDTO dto);
    OptionDTO updateTalla(Integer id, OptionDTO dto);
    void deleteTalla(Integer id);
    OptionDTO findTallaById(Integer id);
    List<OptionDTO> findAllTallas();
    
    // === COLOR ===
    ColorDTO createColor(ColorDTO dto);
    ColorDTO updateColor(Integer id, ColorDTO dto);
    void deleteColor(Integer id);
    ColorDTO findColorById(Integer id);
    List<ColorDTO> findAllColores();
}
