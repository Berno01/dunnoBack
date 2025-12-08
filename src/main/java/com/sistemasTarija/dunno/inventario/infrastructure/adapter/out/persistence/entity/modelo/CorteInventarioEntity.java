package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "corte")
@Getter
@Setter
public class CorteInventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corte")
    private Integer id;

    @Column(name = "nombre_corte")
    private String nombre;

    @Column(name = "estado")
    private Boolean estado;
}
