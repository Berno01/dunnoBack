package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity;

import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.modelo.VarianteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    
    // Relación solo para lectura - no crea columna adicional
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
