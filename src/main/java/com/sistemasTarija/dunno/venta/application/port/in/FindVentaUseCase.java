package com.sistemasTarija.dunno.venta.application.port.in;

import com.sistemasTarija.dunno.venta.application.dto.VentaDTO;
import com.sistemasTarija.dunno.venta.application.dto.VentaFilterDTO;
import com.sistemasTarija.dunno.venta.domain.model.Usuario;
import com.sistemasTarija.dunno.venta.domain.model.Venta;

import java.util.List;
import java.util.Optional;

public interface FindVentaUseCase {
    Optional<VentaDTO> findById(Integer idVenta, Integer idUsuario);
    List<VentaDTO> findAll(VentaFilterDTO filtro, Integer idUsuario);
}
