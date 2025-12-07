package com.sistemasTarija.dunno.recepcion.application.port.in;

public interface DeleteRecepcionUseCase {
    void anularRecepcion(Integer idRecepcion, Integer idUsuario);
}
