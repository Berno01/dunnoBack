package com.sistemasTarija.dunno.catalogo.application.port.in;

import com.sistemasTarija.dunno.catalogo.application.dto.ModeloDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.ModeloListadoDTO;
import com.sistemasTarija.dunno.catalogo.application.dto.RegistrarModeloRequest;

import java.util.List;

public interface ManageModeloUseCase {
    ModeloDTO createModelo(RegistrarModeloRequest request);
    ModeloDTO updateModelo(Integer id, RegistrarModeloRequest request);
    ModeloDTO findModeloById(Integer id);
    List<ModeloDTO> findAllModelos();
    List<ModeloListadoDTO> findAllModelosListado();
    void deleteModelo(Integer id);
}
