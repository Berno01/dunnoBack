package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "modelo_color")
@Getter
@Setter
public class ModeloColorInventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo_color")
    private Integer id;

    @Column(name = "foto_url")
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo")
    private ModeloInventarioEntity modelo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_color")
    private ColorInventarioEntity color;
}
