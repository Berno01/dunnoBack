package com.sistemasTarija.dunno.venta.application.service;

import com.sistemasTarija.dunno.venta.application.port.in.FindUsuarioUseCase;
import com.sistemasTarija.dunno.venta.application.port.out.UsuarioPersistancePort;
import com.sistemasTarija.dunno.venta.domain.exception.UserFailedException;
import com.sistemasTarija.dunno.venta.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsuarioVentaService implements FindUsuarioUseCase {

    private final UsuarioPersistancePort usuarioPersistancePort;

    @Override
    public Usuario findById(Integer idUsuario) {
        return usuarioPersistancePort.findById(idUsuario)
                .orElseThrow(() -> new UserFailedException(
                        "El usuario con ID " + idUsuario + " no fue encontrado en el sistema."
                ));
    }
}
