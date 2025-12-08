package com.sistemasTarija.dunno.auth.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {
    private Integer id;
    private String username;
    private String password;
    private String rol; // "ADMIN" o "VENDEDOR"
    private Integer idSucursal;
    private String nombreCompleto;
    
    public boolean isAdmin() {
        return "ADMIN".equals(this.rol);
    }
}
