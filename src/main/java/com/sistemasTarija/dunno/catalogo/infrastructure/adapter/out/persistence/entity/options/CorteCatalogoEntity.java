package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "corte")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CorteCatalogoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corte")
    private Integer id;

    @Column(name = "nombre_corte")
    private String nombre;

    @Column(name = "estado")
    @Builder.Default
    private Boolean estado = true;
}
