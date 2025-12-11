package com.sistemasTarija.dunno.venta.application.dto.catalogo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DetallePrendaDTO {
    private Integer idModelo;
    private String nombreModelo;
    private Double precio;
    private String nombreMarca;
    private String nombreCategoria;
    private String nombreCorte;
    private Integer stockTotalSucursal;
    private List<ColorDTO> colores = new ArrayList<>();
}