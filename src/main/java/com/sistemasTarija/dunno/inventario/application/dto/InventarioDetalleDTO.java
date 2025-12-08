package com.sistemasTarija.dunno.inventario.application.dto;

import lombok.*;
import java.util.List;

/**
 * DTO para el detalle completo del modelo (Endpoint 2)
 * Contiene toda la información del modelo y sus stocks por sucursal
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventarioDetalleDTO {
    // ID del modelo
    private Integer idModelo;
    
    // Listas de colores y tallas disponibles del modelo
    private List<String> coloresDisponibles;  // ["Azul", "Negro", "Gris"]
    private List<String> tallasDisponibles;   // ["XS", "S", "M", "L", "XL"]
    
    // Datos por sucursal (incluye "Global" como una sucursal especial)
    private List<SucursalStockDTO> sucursales;
}
