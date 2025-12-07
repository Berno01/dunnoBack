package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "marca")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarcaCatalogoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Integer id;

    @Column(name = "nombre_marca")
    private String nombre;

    @Column(name = "estado")
    @Builder.Default
    private Boolean estado = true;
}
