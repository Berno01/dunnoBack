package com.sistemasTarija.dunno.venta.application.port.out;

import com.sistemasTarija.dunno.venta.domain.model.Venta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaPersistancePort {
    Venta save(Venta venta);
    Optional<Venta> findByIdAndSucursal(Integer idVenta, Integer idSucursal);
    List<Venta> findAllByFilters(Integer idSucursal, LocalDateTime fechaInicio, LocalDateTime fechaFin);

}
