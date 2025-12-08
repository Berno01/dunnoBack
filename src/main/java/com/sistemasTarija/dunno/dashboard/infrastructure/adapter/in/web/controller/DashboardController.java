package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.in.web.controller;

import com.sistemasTarija.dunno.dashboard.application.dto.*;
import com.sistemasTarija.dunno.dashboard.application.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard/kpis
     * Retorna: totalVentas, cantidadVentas, ticketPromedio, unidadesVendidas
     */
    @GetMapping("/kpis")
    public ResponseEntity<DashboardKPIsDTO> obtenerKPIs(
            @RequestParam(required = false) Long idSucursal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        DashboardKPIsDTO kpis = dashboardService.obtenerKPIs(idSucursal, fechaInicio, fechaFin);
        return ResponseEntity.ok(kpis);
    }

    /**
     * GET /api/dashboard/ventas-por-hora
     * Retorna: List de hora (0-23) y total (dinero)
     */
    @GetMapping("/ventas-por-hora")
    public ResponseEntity<List<VentasPorHoraDTO>> obtenerVentasPorHora(
            @RequestParam(required = false) Long idSucursal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        List<VentasPorHoraDTO> ventasPorHora = dashboardService.obtenerVentasPorHora(idSucursal, fechaInicio, fechaFin);
        return ResponseEntity.ok(ventasPorHora);
    }

    /**
     * GET /api/dashboard/ventas-por-categoria
     * Retorna: List de categoria y cantidad (ordenado descendente)
     */
    @GetMapping("/ventas-por-categoria")
    public ResponseEntity<List<VentasPorCategoriaDTO>> obtenerVentasPorCategoria(
            @RequestParam(required = false) Long idSucursal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        List<VentasPorCategoriaDTO> ventasPorCategoria = dashboardService.obtenerVentasPorCategoria(idSucursal, fechaInicio, fechaFin);
        return ResponseEntity.ok(ventasPorCategoria);
    }

    /**
     * GET /api/dashboard/metodos-pago
     * Retorna: List de metodo (Efectivo, QR, Tarjeta) y cantidad
     */
    @GetMapping("/metodos-pago")
    public ResponseEntity<List<MetodosPagoDTO>> obtenerMetodosPago(
            @RequestParam(required = false) Long idSucursal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        List<MetodosPagoDTO> metodosPago = dashboardService.obtenerMetodosPago(idSucursal, fechaInicio, fechaFin);
        return ResponseEntity.ok(metodosPago);
    }

    /**
     * GET /api/dashboard/distribucion-tallas
     * Retorna: List de talla (S, M, L, etc.) y cantidad
     */
    @GetMapping("/distribucion-tallas")
    public ResponseEntity<List<DistribucionTallasDTO>> obtenerDistribucionTallas(
            @RequestParam(required = false) Long idSucursal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        List<DistribucionTallasDTO> distribucionTallas = dashboardService.obtenerDistribucionTallas(idSucursal, fechaInicio, fechaFin);
        return ResponseEntity.ok(distribucionTallas);
    }

    /**
     * GET /api/dashboard/top-productos
     * Retorna: List de TopProductoDTO con nombreModelo, subtitulo (Categoria - Marca),
     *          cantidadVendida, stockActual y fotoUrl
     * Parámetro adicional: limit (default 5)
     */
    @GetMapping("/top-productos")
    public ResponseEntity<List<TopProductoDTO>> obtenerTopProductos(
            @RequestParam(required = false) Long idSucursal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<TopProductoDTO> topProductos = dashboardService.obtenerTopProductos(idSucursal, fechaInicio, fechaFin, limit);
        return ResponseEntity.ok(topProductos);
    }
}
