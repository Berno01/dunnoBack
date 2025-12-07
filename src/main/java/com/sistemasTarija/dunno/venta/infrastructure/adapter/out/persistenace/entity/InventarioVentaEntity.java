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
@Table(name = "inventario")
public class InventarioVentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @ManyToOne
    @JoinColumn(name = "id_variante")
    private VarianteEntity variante;
    @Column(name = "id_sucursal")
    private Integer idSucursal;
    @Column(name = "stock_inventario")
    private Integer stockInventario;
    @Column(name = "estado_inventario")
    private Boolean estado;
}
