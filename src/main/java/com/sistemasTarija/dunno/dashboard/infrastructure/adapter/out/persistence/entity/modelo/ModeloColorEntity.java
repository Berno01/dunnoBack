package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "DashboardModeloColor")
@Table(name = "modelo_color")
@Getter
@Setter
public class ModeloColorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo_color")
    private Integer id;

    @Column(name = "foto_url")
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo")
    private ModeloEntity modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_color")
    private ColorEntity color;
}
