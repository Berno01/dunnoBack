package com.sistemasTarija.dunno.venta.domain.model;

import com.sistemasTarija.dunno.venta.domain.exception.VentaFailedException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Venta {
    private Integer idVenta;
    private LocalDateTime fecha;
    private Integer idSucursal;
    private Double total;
    private Double montoEfectivo;
    private Double montoQr;
    private Double montoTarjeta;
    private String tipoVenta;
    private Boolean estadoVenta;
    private List<DetalleVenta> detalleVenta;

    public Venta(Integer idVenta, LocalDateTime fecha, Integer idSucursal, Double montoEfectivo,  Double montoQr, Double montoTarjeta, String tipoVenta, List<DetalleVenta> detalleVenta) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.idSucursal = idSucursal;
        this.detalleVenta = detalleVenta;
        calcularTotal();
        this.montoEfectivo = montoEfectivo;
        this.montoQr = montoQr;
        this.montoTarjeta = montoTarjeta;
        this.tipoVenta = tipoVenta;
        this.estadoVenta = true;
        this.checkMontosPago();
    }


    public void checkMontosPago(){
        Double sumaPagos = this.montoEfectivo + this.montoQr + this.montoTarjeta;
        if(Math.abs(sumaPagos - this.total) > 0.001){
            throw new VentaFailedException("Error: La suma ("+sumaPagos+") no coincide con el total (" + this.total + ")");
        }
    }

    public void actualizarDatos(List<DetalleVenta> nuevosDetalles, Double efectivo, Double qr, Double tarjeta, String tipo) {
        this.detalleVenta = nuevosDetalles;
        this.montoEfectivo = efectivo;
        this.montoQr = qr;
        this.montoTarjeta = tarjeta;
        this.tipoVenta = tipo;
        this.calcularTotal();
        this.checkMontosPago();
    }

    private void calcularTotal() {
        this.total = this.detalleVenta.stream()
                .mapToDouble(DetalleVenta::getTotal)
                .sum();
    }
}
