package com.sistemasTarija.dunno.recepcion.application.port.in;

import com.sistemasTarija.dunno.recepcion.application.dto.RecepcionDTO;
import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;

public interface UpdateRecepcionUseCase {
    Recepcion update(Integer idRecepcion, RecepcionDTO recepcionDTO, Integer idUsuario);
}
