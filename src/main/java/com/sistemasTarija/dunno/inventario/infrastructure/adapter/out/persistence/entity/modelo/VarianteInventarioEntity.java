package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "variante")
@Getter
@Setter
public class VarianteInventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelo_color")
    private ModeloColorInventarioEntity modeloColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_talla")
    private TallaInventarioEntity talla;
}
