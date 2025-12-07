package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.modelo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "corte")
@Getter
@Setter
public class CorteEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corte")
    private Integer id;

    @Column(name = "nombre_corte")
    private String nombre;
}