package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.modelo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "modelo") @Getter @Setter
public class ModeloEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Integer id;

    @Column(name = "nombre_modelo")
    private String nombre;

    @Column(name = "precio")
    private Double precio;

    @ManyToOne @JoinColumn(name = "id_marca")
    private MarcaEntity marca;

    @ManyToOne @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;

    @ManyToOne @JoinColumn(name = "id_corte")
    private CorteEntity corte;

    @Column(name = "estado")
    private Boolean estado;
}