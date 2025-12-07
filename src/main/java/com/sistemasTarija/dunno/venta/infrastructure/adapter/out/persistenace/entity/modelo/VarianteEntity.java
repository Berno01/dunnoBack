package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.modelo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "variante") @Getter @Setter
public class VarianteEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante")
    private Integer id;

    @ManyToOne @JoinColumn(name = "id_modelo_color")
    private ModeloColorEntity modeloColor;

    @ManyToOne @JoinColumn(name = "id_talla")
    private TallaEntity talla;
}