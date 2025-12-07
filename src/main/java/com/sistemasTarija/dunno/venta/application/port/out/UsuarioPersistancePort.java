package com.sistemasTarija.dunno.venta.application.port.out;

import com.sistemasTarija.dunno.venta.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioPersistancePort {
    Optional<Usuario> findById(Integer idUsuario);
}
