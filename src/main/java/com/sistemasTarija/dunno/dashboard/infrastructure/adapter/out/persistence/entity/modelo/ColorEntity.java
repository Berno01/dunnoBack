package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "DashboardColor")
@Table(name = "color")
@Getter
@Setter
public class ColorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_color")
    private Integer id;

    @Column(name = "nombre_color")
    private String nombre;
}
