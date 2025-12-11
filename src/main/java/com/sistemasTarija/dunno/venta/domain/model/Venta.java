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
    private Double montoGiftcard;
    private Double descuento;
    private String tipoDescuento;
    private String tipoVenta;
    private Boolean estadoVenta;
    private LocalDateTime createdAt;
    private Integer createdBy;
    private LocalDateTime updatedAt;
    private Integer updatedBy;
    private List<DetalleVenta> detalleVenta;

    public Venta(Integer idVenta, LocalDateTime fecha, Integer idSucursal, Double montoEfectivo,  Double montoQr, Double montoTarjeta, Double montoGiftcard, Double descuento, String tipoDescuento, String tipoVenta, List<DetalleVenta> detalleVenta) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.idSucursal = idSucursal;
        this.detalleVenta = detalleVenta;
        this.descuento = descuento != null ? descuento : 0.0;
        calcularTotal();
        this.montoEfectivo = montoEfectivo != null ? montoEfectivo : 0.0;
        this.montoQr = montoQr != null ? montoQr : 0.0;
        this.montoTarjeta = montoTarjeta != null ? montoTarjeta : 0.0;
        this.montoGiftcard = montoGiftcard != null ? montoGiftcard : 0.0;
        this.tipoDescuento = tipoDescuento;
        this.tipoVenta = tipoVenta;
        this.estadoVenta = true;
        this.checkMontosPago();
    }


    public void checkMontosPago(){
        if (this.descuento >= this.total) throw new VentaFailedException("Error: El descuento es superior a la venta");
        Double sumaPagos = this.montoEfectivo + this.montoQr + this.montoTarjeta + this.montoGiftcard;
        if(Math.abs(sumaPagos - (this.total)) > 0.001){
            throw new VentaFailedException("Error: La suma ("+sumaPagos+") no coincide con el total (" + this.total + ")");
        }
    }

    public void actualizarDatos(List<DetalleVenta> nuevosDetalles, Double efectivo, Double qr, Double tarjeta, Double giftcard, Double descuento, String tipoDescuento, String tipo) {
        this.detalleVenta = nuevosDetalles;
        this.montoEfectivo = efectivo != null ? efectivo : 0.0;
        this.montoQr = qr != null ? qr : 0.0;
        this.montoTarjeta = tarjeta != null ? tarjeta : 0.0;
        this.montoGiftcard = giftcard != null ? giftcard : 0.0;
        this.descuento = descuento != null ? descuento : 0.0;
        this.tipoDescuento = tipoDescuento;
        this.tipoVenta = tipo;
        this.calcularTotal();
        this.checkMontosPago();
    }

    private void calcularTotal() {
        double subtotal = this.detalleVenta.stream()
                .mapToDouble(DetalleVenta::getTotal)
                .sum();
        this.total = subtotal - this.descuento;
    }
}
