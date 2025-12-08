package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "talla")
@Getter
@Setter
public class TallaInventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_talla")
    private Integer id;

    @Column(name = "nombre_talla")
    private String nombre;

    @Column(name = "estado")
    private Boolean estado;
}
