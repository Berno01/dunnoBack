package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace;

import com.sistemasTarija.dunno.venta.application.port.out.UsuarioPersistancePort;
import com.sistemasTarija.dunno.venta.domain.model.Usuario;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.mapper.UsuarioVentaPersistanceMapper;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository.UsuarioVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioVentaRepositoryAdapter implements UsuarioPersistancePort {
    private final UsuarioVentaRepository repository;
    private final UsuarioVentaPersistanceMapper mapper;


    @Override
    public Optional<Usuario> findById(Integer idUsuario) {
        return repository.findById(idUsuario)
                .map(mapper::toDomain);
    }
}
