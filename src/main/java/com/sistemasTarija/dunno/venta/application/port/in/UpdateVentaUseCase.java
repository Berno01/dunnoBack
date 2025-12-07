package com.sistemasTarija.dunno.venta.application.port.in;

import com.sistemasTarija.dunno.venta.application.dto.VentaDTO;

public interface UpdateVentaUseCase {
    VentaDTO update(Integer idVenta, VentaDTO ventaDTO, Integer idUsuario);
}
