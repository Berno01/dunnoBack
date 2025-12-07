package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recepcion")
public class RecepcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcion")
    private Integer idRecepcion;

    @Column(name = "fecha_recepcion")
    private LocalDateTime fecha;

    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "estado")
    private Boolean estado;

    @OneToMany(
            mappedBy = "recepcion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleRecepcionEntity> detalles;
}
