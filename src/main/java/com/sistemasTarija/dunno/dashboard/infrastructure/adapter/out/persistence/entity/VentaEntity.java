package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
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
@Entity(name = "DashboardVenta")
@Table(name = "venta")
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_venta;

    @Column(name = "fecha_venta")
    private LocalDateTime fecha;
    
    @Column(name = "id_sucursal")
    private Integer idSucursal;
    
    @Column(name = "total_venta")
    private Double total;

    @Column(name = "monto_efectivo")
    private Double montoEfectivo;
    
    @Column(name = "monto_qr")
    private Double montoQr;
    
    @Column(name = "monto_tarjeta")
    private Double montoTarjeta;
    
    @Column(name = "tipo_venta")
    private String tipoVenta;
    
    @Column(name = "estado_venta")
    private Boolean estadoVenta;

    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY)
    private List<DetalleVentaEntity> detalleVenta;
}
