package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.modelo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "marca")
@Getter
@Setter
public class MarcaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Integer id;

    @Column(name = "nombre_marca")
    private String nombre;
}