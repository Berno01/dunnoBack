package com.sistemasTarija.dunno.venta.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private Integer idUsuario;
    private String username;
    private String rol;
    private Integer idSucursal;


    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.rol);
    }
}
