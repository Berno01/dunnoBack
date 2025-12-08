package com.sistemasTarija.dunno.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    @JsonProperty("id_usuario")
    private Integer idUsuario;
    
    private String rol;
    
    @JsonProperty("id_sucursal")
    private Integer idSucursal;
    
    @JsonProperty("nombre_completo")
    private String nombreCompleto;
    
    private String username;
}
