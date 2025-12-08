package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.entity.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "DashboardVariante")
@Table(name = "variante")
@Getter
@Setter
public class VarianteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modelo_color")
    private ModeloColorEntity modeloColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_talla")
    private TallaEntity talla;
}
