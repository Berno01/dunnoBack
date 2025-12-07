package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.CategoriaCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.CorteCatalogoEntity;
import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.MarcaCatalogoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modelo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModeloCatalogoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Integer id;

    @Column(name = "nombre_modelo")
    private String nombre;

    @Column(name = "id_marca")
    private Integer idMarca;

    @ManyToOne
    @JoinColumn(name = "id_marca", insertable = false, updatable = false)
    private MarcaCatalogoEntity marca;

    @Column(name = "id_categoria")
    private Integer idCategoria;

    @ManyToOne
    @JoinColumn(name = "id_categoria", insertable = false, updatable = false)
    private CategoriaCatalogoEntity categoria;

    @Column(name = "id_corte")
    private Integer idCorte;

    @ManyToOne
    @JoinColumn(name = "id_corte", insertable = false, updatable = false)
    private CorteCatalogoEntity corte;

    @Column(name = "estado")
    @Builder.Default
    private Boolean estado = true;

    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ModeloColorCatalogoEntity> colores = new ArrayList<>();
}
