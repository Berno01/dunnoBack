package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario") // Nombre de tu tabla en DB
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioVentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "rol") // "ADMIN" o "VENDEDOR"
    private String rol;

    @Column(name = "id_sucursal") // Puede ser NULL (para admins)
    private Integer idSucursal;

    @Column(name = "nombre_completo")
    private String nombreCompleto;
}
