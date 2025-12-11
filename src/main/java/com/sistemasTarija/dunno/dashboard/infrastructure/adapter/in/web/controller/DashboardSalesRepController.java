package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.in.web.controller;

import com.sistemasTarija.dunno.dashboard.application.dto.DashboardFilterDTO;
import com.sistemasTarija.dunno.dashboard.application.dto.DashboardSalesRepResponseDTO;
import com.sistemasTarija.dunno.dashboard.application.port.in.GetDashboardSalesRepUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controlador REST para el Dashboard de Análisis de Vendedores.
 * 
 * Endpoints:
 * - GET /api/dashboard/sales-rep-analysis: Obtiene todos los datos del dashboard
 * 
 * Parámetros de filtrado:
 * - startDate: Fecha de inicio del rango (opcional)
 * - endDate: Fecha de fin del rango (opcional)
 * - salesRepId: ID del vendedor para filtrar (opcional)
 * 
 * Comportamiento:
 * - Si NO se envía salesRepId: Muestra datos de todos los vendedores
 * - Si SÍ se envía salesRepId: Filtra todos los datos para ese vendedor específico
 * - El usuario no admin solo verá datos de su sucursal
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardSalesRepController {
    
    private final GetDashboardSalesRepUseCase getDashboardUseCase;
    
    /**
     * Obtiene todos los datos del dashboard de vendedores aplicando filtros opcionales.
     * 
     * @param startDate Fecha de inicio del rango (formato: yyyy-MM-dd)
     * @param endDate Fecha de fin del rango (formato: yyyy-MM-dd)
     * @param salesRepId ID del vendedor para filtrar (opcional)
     * @param idUsuario ID del usuario que hace la petición (del header)
     * @return DTO completo con todas las secciones del dashboard
     */
    @GetMapping("/sales-rep-analysis")
    public ResponseEntity<DashboardSalesRepResponseDTO> getSalesRepAnalysis(
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        
        @RequestParam(required = false) Integer salesRepId,
        
        @RequestHeader("X-Usuario-Id") Integer idUsuario
    ) {
        log.info("GET /api/dashboard/sales-rep-analysis - Usuario: {}, Fechas: {} a {}, SalesRep: {}", 
            idUsuario, startDate, endDate, salesRepId);
        
        // Construir el DTO de filtros
        DashboardFilterDTO filter = DashboardFilterDTO.builder()
            .startDate(startDate)
            .endDate(endDate)
            .salesRepId(salesRepId)
            .build();
        
        // TODO: Aquí deberías verificar si el usuario es admin o no
        // Si no es admin, agregar el filtro de sucursal:
        // Usuario usuario = usuarioService.findById(idUsuario);
        // if (!usuario.isAdmin()) {
        //     filter.setIdSucursal(usuario.getIdSucursal());
        // }
        
        // Ejecutar el caso de uso
        DashboardSalesRepResponseDTO response = getDashboardUseCase.getDashboardData(filter);
        
        log.info("Dashboard obtenido exitosamente para usuario {}", idUsuario);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Variante del endpoint que acepta el filtro como RequestBody (para filtros más complejos).
     * Útil si en el futuro necesitas agregar más filtros.
     */
    @PostMapping("/sales-rep-analysis")
    public ResponseEntity<DashboardSalesRepResponseDTO> getSalesRepAnalysisPost(
        @RequestBody DashboardFilterDTO filter,
        @RequestHeader("X-Usuario-Id") Integer idUsuario
    ) {
        log.info("POST /api/dashboard/sales-rep-analysis - Usuario: {}, Filtros: {}", 
            idUsuario, filter);
        
        // TODO: Aplicar filtro de sucursal si el usuario no es admin
        
        DashboardSalesRepResponseDTO response = getDashboardUseCase.getDashboardData(filter);
        
        log.info("Dashboard obtenido exitosamente para usuario {}", idUsuario);
        return ResponseEntity.ok(response);
    }
}
