package com.sistemasTarija.dunno.catalogo.infrastructure.adapter.out.persistence.entity.options;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "color")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColorCatalogoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_color")
    private Integer id;

    @Column(name = "nombre_color")
    private String nombre;

    @Column(name = "codigo_hex_color")
    private String codigoHex;

    @Column(name = "estado")
    @Builder.Default
    private Boolean estado = true;
}
