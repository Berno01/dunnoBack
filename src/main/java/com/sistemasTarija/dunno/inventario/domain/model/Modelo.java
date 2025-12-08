package com.sistemasTarija.dunno.inventario.domain.model;

import com.sistemasTarija.dunno.inventario.domain.model.options.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Modelo {
    private Integer id;
    private String nombre;
    
    // Referencias a entidades relacionadas (usando IDs para independencia del dominio)
    private Integer idMarca;
    private Integer idCategoria;
    private Integer idCorte;
    
    // Objetos completos (opcionalmente cargados)
    private Marca marca;
    private Categoria categoria;
    private Corte corte;

    @Builder.Default
    private Boolean estado = true;
    
    // Agregado: Lista de colores del modelo
    @Builder.Default
    private List<ModeloColor> colores = new ArrayList<>();
}
