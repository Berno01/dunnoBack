package com.sistemasTarija.dunno.venta.application.port.in;

import com.sistemasTarija.dunno.venta.domain.model.Usuario;

import java.util.Optional;

public interface FindUsuarioUseCase {
    Usuario findById(Integer idUsuario);
}
