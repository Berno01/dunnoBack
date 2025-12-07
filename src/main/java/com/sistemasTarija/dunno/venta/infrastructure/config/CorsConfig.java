package com.sistemasTarija.dunno.venta.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Cambié /api/** por /** para asegurar que cubra todo
                .allowedOrigins(
                        "http://localhost:4200",                 // Tu Angular local
                        "https://fastidious-maamoul-cda035.netlify.app" // <--- ¡PEGA AQUÍ TU LINK DE NETLIFY!
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
