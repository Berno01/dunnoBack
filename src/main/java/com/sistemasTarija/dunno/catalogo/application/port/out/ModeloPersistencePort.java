package com.sistemasTarija.dunno.catalogo.application.port.out;

import com.sistemasTarija.dunno.catalogo.domain.model.Modelo;

import java.util.List;
import java.util.Optional;

public interface ModeloPersistencePort {
    Modelo saveModelo(Modelo modelo);
    Optional<Modelo> findById(Integer id);
    List<Modelo> findAll();
    boolean existsByNombre(String nombre);
}
