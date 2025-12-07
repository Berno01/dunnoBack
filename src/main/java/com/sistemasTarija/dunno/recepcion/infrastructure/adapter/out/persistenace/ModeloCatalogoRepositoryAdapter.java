package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace;

import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ResumenModeloDTO;
import com.sistemasTarija.dunno.recepcion.application.port.out.ModeloCatalogoPersistancePort;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.dto.ModeloRawDTO;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.repository.ModeloCatalogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ModeloCatalogoRepositoryAdapter implements ModeloCatalogoPersistancePort {
    
    private final ModeloCatalogoRepository repository;

    @Override
    public List<ResumenModeloDTO> obtenerListadoModelos() {
        return repository.obtenerListadoModelos();
    }

    @Override
    public List<ModeloRawDTO> obtenerDetalleModeloRaw(Integer idModelo) {
        return repository.obtenerDetalleModeloRaw(idModelo);
    }
}
