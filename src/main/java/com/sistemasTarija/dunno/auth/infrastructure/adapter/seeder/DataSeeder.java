package com.sistemasTarija.dunno.auth.infrastructure.adapter.seeder;

import com.sistemasTarija.dunno.auth.application.port.out.UsuarioPersistencePort;
import com.sistemasTarija.dunno.auth.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Carga inicial de datos en la tabla usuario
 * 
 * Se ejecuta al iniciar la aplicación y crea dos usuarios por defecto:
 * 1. berno - ADMIN (sin sucursal asignada)
 * 2. vendedor1 - VENDEDOR (sucursal 1)
 * 
 * Ambos con contraseña: 123
 */
@Slf4j
@Component
@Profile("!test") // No se ejecuta durante los tests
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    
    private final UsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        log.info("Iniciando carga de datos iniciales...");
        
        // Usuario 1: Administrador
        crearOActualizarUsuario("berno", "123", "ADMIN", null, "Bernardo Administrador");
        crearOActualizarUsuario("dunnoadmin", "du2025nno", "ADMIN", null, "Administrador");

        // Usuario 2: Vendedor de sucursal 1
        crearOActualizarUsuario("dunnotja", "du2025nno", "VENDEDOR", 1, "Vendedor Uno Tja");
        crearOActualizarUsuario("dunnotja2", "du2025nno", "VENDEDOR", 1, "Vendedor Dos Tja");
        crearOActualizarUsuario("dunnocbba", "du2025nno", "VENDEDOR", 2, "Vendedor Uno Cbba");
        crearOActualizarUsuario("dunnoscz", "du2025nno", "VENDEDOR", 3, "Vendedor Uno SCZ");


        log.info("Carga de datos iniciales completada.");
    }
    
    /**
     * Crea un usuario si no existe, o actualiza su contraseña si está en texto plano
     */
    private void crearOActualizarUsuario(String username, String rawPassword, String rol, 
                                          Integer idSucursal, String nombreCompleto) {
        var usuarioOpt = usuarioPersistencePort.findByUsername(username);
        
        if (usuarioOpt.isEmpty()) {
            // Crear nuevo usuario
            Usuario nuevoUsuario = Usuario.builder()
                    .username(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .rol(rol)
                    .idSucursal(idSucursal)
                    .nombreCompleto(nombreCompleto)
                    .build();
            
            usuarioPersistencePort.save(nuevoUsuario);
            log.info("✓ Usuario '{}' ({}) creado exitosamente", username, rol);
        } else {
            Usuario usuario = usuarioOpt.get();
            
            // Verificar si la contraseña está en texto plano (no comienza con $2a$ de BCrypt)
            if (!usuario.getPassword().startsWith("$2a$")) {
                usuario.setPassword(passwordEncoder.encode(rawPassword));
                usuarioPersistencePort.save(usuario);
                log.info("⚠ Usuario '{}' actualizado - contraseña hasheada con BCrypt", username);
            } else {
                log.info("→ Usuario '{}' ya existe con contraseña hasheada", username);
            }
        }
    }
}
