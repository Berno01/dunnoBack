package com.sistemasTarija.dunno.auth.infrastructure.adapter.controller;

import com.sistemasTarija.dunno.auth.application.dto.LoginRequest;
import com.sistemasTarija.dunno.auth.application.dto.UsuarioDTO;
import com.sistemasTarija.dunno.auth.application.port.in.AuthUseCase;
import com.sistemasTarija.dunno.auth.domain.exception.AuthenticationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para autenticación
 * 
 * Endpoints:
 * POST /api/auth/login - Autenticar usuario
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthUseCase authUseCase;
    
    /**
     * Endpoint de login
     * 
     * Request body:
     * {
     *   "username": "berno",
     *   "password": "123"
     * }
     * 
     * Response (200 OK):
     * {
     *   "id_usuario": 1,
     *   "username": "berno",
     *   "rol": "ADMIN",
     *   "id_sucursal": null,
     *   "nombre_completo": "Bernardo Administrador"
     * }
     * 
     * Response (401 Unauthorized):
     * {
     *   "error": "Usuario no encontrado" | "Contraseña incorrecta"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            UsuarioDTO usuario = authUseCase.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(usuario);
        } catch (AuthenticationFailedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
