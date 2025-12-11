package com.sistemasTarija.dunno.dashboard.application.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO para filtros del dashboard
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardFilterDTO {
    
    /**
     * Fecha de inicio del rango (opcional)
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    
    /**
     * Fecha de fin del rango (opcional)
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    
    /**
     * ID del vendedor para filtrar (opcional)
     * Si es null, muestra datos de todos los vendedores
     */
    private Integer salesRepId;
    
    /**
     * ID de la sucursal para filtrar (opcional)
     * Se usa para usuarios no admin
     */
    private Integer idSucursal;
}
