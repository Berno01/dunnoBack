package com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence;

import com.sistemasTarija.dunno.auth.application.port.out.UsuarioPersistencePort;
import com.sistemasTarija.dunno.auth.domain.model.Usuario;
import com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence.mapper.UsuarioPersistenceMapper;
import com.sistemasTarija.dunno.auth.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioPersistencePort {
    
    private final UsuarioRepository usuarioRepository;
    private final UsuarioPersistenceMapper mapper;
    
    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(mapper::toDomain);
    }
    
    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity savedEntity = usuarioRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
}
