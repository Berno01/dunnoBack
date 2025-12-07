package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.ColorCatalogoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modelo_color")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModeloColorCatalogoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo_color")
    private Integer id;

    @Column(name = "foto_url")
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "id_modelo")
    private ModeloCatalogoEntity modelo;

    @Column(name = "id_color")
    private Integer idColor;

    @ManyToOne
    @JoinColumn(name = "id_color", insertable = false, updatable = false)
    private ColorCatalogoEntity color;

    @OneToMany(mappedBy = "modeloColor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VarianteCatalogoEntity> variantes = new ArrayList<>();
}
