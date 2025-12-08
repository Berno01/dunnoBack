package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity;

import com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity.modelo.VarianteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DashboardDetalleVenta")
@Table(name = "detalle_venta")
public class DetalleVentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle_venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    private VentaEntity venta;

    @Column(name = "id_variante")
    private Integer idVariante;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variante", insertable = false, updatable = false)
    private VarianteEntity variante;
    
    @Column(name = "cantidad")
    private Integer cantidad;
    
    @Column(name = "precio_unitario")
    private Double precioUnitario;
    
    @Column(name = "total")
    private Double total;
}
