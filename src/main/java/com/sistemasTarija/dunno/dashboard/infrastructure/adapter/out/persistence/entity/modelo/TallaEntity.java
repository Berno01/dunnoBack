package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "DashboardTalla")
@Table(name = "talla")
@Getter
@Setter
public class TallaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_talla")
    private Integer id;

    @Column(name = "nombre_talla")
    private String nombre;
}
