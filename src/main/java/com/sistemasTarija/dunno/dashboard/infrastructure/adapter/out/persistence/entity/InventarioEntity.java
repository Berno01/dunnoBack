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
@Entity(name = "DashboardInventario")
@Table(name = "inventario")
public class InventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variante")
    private VarianteEntity variante;
    
    @Column(name = "id_sucursal")
    private Integer idSucursal;
    
    @Column(name = "stock_inventario")
    private Integer stockInventario;
    
    @Column(name = "estado_inventario")
    private Boolean estado;
}
