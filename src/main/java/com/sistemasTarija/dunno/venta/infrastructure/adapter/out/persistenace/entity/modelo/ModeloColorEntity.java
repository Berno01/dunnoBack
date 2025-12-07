package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.modelo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "modelo_color") @Getter @Setter
public class ModeloColorEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo_color")
    private Integer id;

    @Column(name = "foto_url")
    private String fotoUrl;

    @ManyToOne @JoinColumn(name = "id_modelo")
    private ModeloEntity modelo;

    @ManyToOne @JoinColumn(name = "id_color")
    private ColorEntity color;
}