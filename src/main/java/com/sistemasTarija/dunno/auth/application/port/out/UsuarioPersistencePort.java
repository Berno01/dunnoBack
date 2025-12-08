package com.sistemasTarija.dunno.auth.application.port.out;

import com.sistemasTarija.dunno.auth.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioPersistencePort {
    
    Optional<Usuario> findByUsername(String username);
    
    Usuario save(Usuario usuario);
    
    boolean existsByUsername(String username);
}
