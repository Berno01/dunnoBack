package com.sistemasTarija.dunno.venta.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VentaDTO {


    @JsonProperty("id_venta")
    private Integer idVenta;
    @JsonProperty("fecha_venta")
    private LocalDateTime fecha;
    @JsonProperty("id_sucursal")
    private Integer idSucursal;
    @JsonProperty("total")
    private Double total;

    @JsonProperty("monto_efectivo")
    private Double montoEfectivo;
    @JsonProperty("monto_qr")
    private Double montoQr;
    @JsonProperty("monto_tarjeta")
    private Double montoTarjeta;
    @JsonProperty("tipo_venta")
    private String tipo;



    @JsonProperty("detalle_venta")
    private List<DetalleVentaDTO> detalleVenta;

    public VentaDTO() {
    }
}
