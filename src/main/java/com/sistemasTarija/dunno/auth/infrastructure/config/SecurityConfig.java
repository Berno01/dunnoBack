package com.sistemasTarija.dunno.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security
 * 
 * Objetivo: Usar BCryptPasswordEncoder para encriptar contraseñas,
 * pero SIN usar filtros HTTP de Spring Security.
 * Toda la lógica de autenticación se maneja en AuthService.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * Bean para encriptar/verificar contraseñas con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configuración del SecurityFilterChain
     * - Permite acceso público a TODOS los endpoints
     * - Desactiva CSRF (no necesario para APIs REST stateless)
     * - Desactiva CORS desde Spring Security (se puede manejar en otro lugar si se necesita)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF
            .cors(cors -> cors.disable()) // Desactivar CORS desde Security
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permitir acceso público a todo
            );
        
        return http.build();
    }
}
