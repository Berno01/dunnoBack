package com.sistemasTarija.dunno.recepcion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionResumen {
    private Integer idRecepcion;
    private LocalDateTime fecha;
    private Integer idSucursal;
    private Boolean estado;
    private Long totalItems;
    private java.util.Set<String> categorias;
    private String marca; // Marca del primer item
}
