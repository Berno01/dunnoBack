package com.sistemasTarija.dunno.venta.application.port.in;

import com.sistemasTarija.dunno.venta.domain.model.Venta;
import com.sistemasTarija.dunno.venta.application.dto.VentaDTO;

public interface CreateVentaUseCase {
    Venta save(VentaDTO ventaDTO);
}
