package com.sistemasTarija.dunno.venta.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DetalleVentaDTO {

    @JsonProperty("id_variante")
    private Integer idVariante;
    
    @JsonProperty("id_modelo")
    private Integer idModelo; // Solo para respuesta - ignorado en request
    
    @JsonProperty("cantidad")
    private Integer cantidad;
    @JsonProperty("precio_unitario")
    private Double precioUnitario;
    @JsonProperty("total")
    private Double total;


    public DetalleVentaDTO() {}
    
    // Constructor sin idModelo (para crear desde requests)
    public DetalleVentaDTO(Integer idVariante, Integer cantidad, Double precioUnitario, Double total) {
        this.idVariante = idVariante;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = total;
        this.idModelo = null; // Se llenará después en lectura
    }
}
