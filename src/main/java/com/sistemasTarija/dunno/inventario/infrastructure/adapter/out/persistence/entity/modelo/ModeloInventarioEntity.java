package com.sistemasTarija.dunno.inventario.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "modelo")
@Getter
@Setter
public class ModeloInventarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Integer id;

    @Column(name = "nombre_modelo")
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_marca")
    private MarcaInventarioEntity marca;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    private CategoriaInventarioEntity categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_corte")
    private CorteInventarioEntity corte;
    
    @Column(name = "estado")
    private Boolean estado;
}
