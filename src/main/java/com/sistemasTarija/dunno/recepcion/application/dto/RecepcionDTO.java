package com.sistemasTarija.dunno.recepcion.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecepcionDTO {
    @JsonProperty("idRecepcion")
    private Integer idRecepcion;

    @JsonProperty("fecha")
    private LocalDateTime fecha;

    @JsonProperty("idSucursal")
    private Integer idSucursal;

    @JsonProperty("estado")
    private Boolean estado;

    @JsonProperty("totalItems")
    private Long totalItems;

    @JsonProperty("marca")
    private String marca;

    @JsonProperty("categorias")
    private java.util.Set<String> categorias;

    @JsonProperty("detalles")
    private List<DetalleRecepcionDTO> detalles;
}
