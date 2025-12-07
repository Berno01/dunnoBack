package com.sistemasTarija.dunno.recepcion.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Recepcion {
    private Integer idRecepcion;
    private LocalDateTime fecha;
    private Integer idSucursal;
    private Boolean estado;
    private List<DetalleRecepcion> detalles;

    public Recepcion(Integer idRecepcion, LocalDateTime fecha, Integer idSucursal, List<DetalleRecepcion> detalles) {
        this.idRecepcion = idRecepcion;
        this.fecha = fecha;
        this.idSucursal = idSucursal;
        this.detalles = detalles;
        this.estado = true;
    }

    public void anular() {
        this.estado = false;
    }
}
