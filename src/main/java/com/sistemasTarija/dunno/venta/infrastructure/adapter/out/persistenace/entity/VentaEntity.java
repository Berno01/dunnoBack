package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity;

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
@Entity
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
    @Column(name = "monto_giftcard")
    private Double montoGiftcard;
    @Column(name = "descuento")
    private Double descuento;
    @Column(name = "tipo_descuento")
    private String tipoDescuento;
    @Column(name = "tipo_venta")
    private String tipoVenta;
    @Column(name = "estado_venta")
    private Boolean estadoVenta;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "updated_by")
    private Integer updatedBy;

    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleVentaEntity> detalleVenta;
}
