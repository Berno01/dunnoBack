package com.sistemasTarija.dunno.venta.application.port.in;

public interface DeleteVentaUseCase {
    void delete(Integer idVenta, Integer idUsuario);
    void activate(Integer idVenta, Integer idUsuario);
}
