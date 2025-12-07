package com.sistemasTarija.dunno.venta.application.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResumenPrendaDTO {
    private Integer idModelo;
    private String nombreModelo;
    private String nombreMarca;
    private String nombreCategoria;
    private String fotoPortada; // Una sola foto representativa
    private Long stockTotal; // La suma total (Long porque SQL SUM devuelve Long)
    private Boolean pocasUnidades; // Calculado: true si stock < 5
}
