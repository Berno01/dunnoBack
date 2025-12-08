package com.sistemasTarija.dunno.auth.application.port.in;

import com.sistemasTarija.dunno.auth.application.dto.UsuarioDTO;

public interface AuthUseCase {
    
    /**
     * Autentica un usuario con username y contraseña
     * 
     * @param username nombre de usuario
     * @param rawPassword contraseña en texto plano
     * @return DTO del usuario autenticado (sin password)
     * @throws com.sistemasTarija.dunno.auth.domain.exception.AuthenticationFailedException si las credenciales son inválidas
     */
    UsuarioDTO login(String username, String rawPassword);
}
