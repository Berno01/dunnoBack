package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity;

import com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options.TallaCatalogoEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "variante")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VarianteCatalogoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_modelo_color")
    private ModeloColorCatalogoEntity modeloColor;

    @Column(name = "id_talla")
    private Integer idTalla;

    @ManyToOne
    @JoinColumn(name = "id_talla", insertable = false, updatable = false)
    private TallaCatalogoEntity talla;
}
