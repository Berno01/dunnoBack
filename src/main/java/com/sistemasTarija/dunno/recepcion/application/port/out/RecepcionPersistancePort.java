package com.sistemasTarija.dunno.recepcion.application.port.out;

import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecepcionPersistancePort {
    Recepcion save(Recepcion recepcion);
    Optional<Recepcion> findByIdAndSucursal(Integer idRecepcion, Integer idSucursal);
    List<Recepcion> findAllByFilters(Integer idSucursal, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<com.sistemasTarija.dunno.recepcion.domain.model.RecepcionResumen> findAllResumenByFilters(Integer idSucursal, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
