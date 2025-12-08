package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity;

import com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo.VarianteInventarioEntity;
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
public class InventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variante")
    private VarianteInventarioEntity variante;
    
    @Column(name = "id_sucursal")
    private Integer idSucursal;
    
    @Column(name = "stock_inventario")
    private Integer stockInventario;
    
    @Column(name = "estado_inventario")
    private Boolean estado;
}
