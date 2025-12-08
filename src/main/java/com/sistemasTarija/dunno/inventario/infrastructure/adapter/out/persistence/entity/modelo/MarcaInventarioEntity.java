package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "marca")
@Getter
@Setter
public class MarcaInventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Integer id;

    @Column(name = "nombre_marca")
    private String nombre;

    @Column(name = "estado")
    private Boolean estado;
}
