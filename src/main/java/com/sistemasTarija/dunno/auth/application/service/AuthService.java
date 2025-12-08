package com.sistemasTarija.dunno.auth.application.service;

import com.sistemasTarija.dunno.auth.application.dto.UsuarioDTO;
import com.sistemasTarija.dunno.auth.application.mapper.UsuarioMapper;
import com.sistemasTarija.dunno.auth.application.port.in.AuthUseCase;
import com.sistemasTarija.dunno.auth.application.port.out.UsuarioPersistencePort;
import com.sistemasTarija.dunno.auth.domain.exception.AuthenticationFailedException;
import com.sistemasTarija.dunno.auth.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación
 * 
 * Maneja la lógica de login usando BCryptPasswordEncoder
 * para verificar las contraseñas hasheadas.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {
    
    private final UsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    
    @Override
    public UsuarioDTO login(String username, String rawPassword) {
        // 1. Buscar usuario por username
        Usuario usuario = usuarioPersistencePort.findByUsername(username)
                .orElseThrow(() -> new AuthenticationFailedException("Usuario no encontrado"));
        
        // 2. Verificar contraseña con BCrypt
        boolean passwordMatches = passwordEncoder.matches(rawPassword, usuario.getPassword());
        
        if (!passwordMatches) {
            throw new AuthenticationFailedException("Contraseña incorrecta");
        }
        
        // 3. Retornar DTO (sin el password)
        return usuarioMapper.toDTO(usuario);
    }
}
