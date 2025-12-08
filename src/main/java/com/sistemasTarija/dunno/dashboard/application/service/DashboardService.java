package com.sistemasTarija.dunno.dashboard.application.service;

import com.sistemasTarija.dunno.dashboard.application.dto.*;
import com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    /**
     * Obtiene los KPIs del dashboard
     */
    public DashboardKPIsDTO obtenerKPIs(Long idSucursal, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = obtenerFechaInicio(fechaInicio);
        LocalDateTime fin = obtenerFechaFin(fechaFin);
        
        Object resultado = dashboardRepository.obtenerKPIsRaw(idSucursal, inicio, fin);
        Object[] row = (Object[]) resultado;
        
        Double totalVentas = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
        Long cantidadVentas = row[1] != null ? ((Number) row[1]).longValue() : 0L;
        Double ticketPromedio = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
        Long unidadesVendidas = row[3] != null ? ((Number) row[3]).longValue() : 0L;
        
        return new DashboardKPIsDTO(totalVentas, cantidadVentas, ticketPromedio, unidadesVendidas);
    }

    /**
     * Obtiene las ventas agrupadas por hora
     * Cuenta cantidad de ventas (no monto total)
     * Si hay múltiples días, promedia las ventas por hora
     * Rango dinámico: trae todas las horas donde hay ventas registradas
     */
    public List<VentasPorHoraDTO> obtenerVentasPorHora(Long idSucursal, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = obtenerFechaInicio(fechaInicio);
        LocalDateTime fin = obtenerFechaFin(fechaFin);
        
        List<Object[]> resultados = dashboardRepository.obtenerVentasPorHoraRaw(idSucursal, inicio, fin);
        List<VentasPorHoraDTO> ventasPorHora = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            Integer hora = ((Number) resultado[0]).intValue();
            Long cantidadVentas = ((Number) resultado[1]).longValue();
            Long diasUnicos = ((Number) resultado[2]).longValue();
            
            // Si hay múltiples días, promediamos las ventas por hora
            Double promedioCantidad = diasUnicos > 0 ? (double) cantidadVentas / diasUnicos : 0.0;
            
            ventasPorHora.add(new VentasPorHoraDTO(hora, promedioCantidad));
        }
        
        return ventasPorHora;
    }

    /**
     * Obtiene las ventas agrupadas por categoría
     */
    public List<VentasPorCategoriaDTO> obtenerVentasPorCategoria(Long idSucursal, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = obtenerFechaInicio(fechaInicio);
        LocalDateTime fin = obtenerFechaFin(fechaFin);
        
        List<Object[]> resultados = dashboardRepository.obtenerVentasPorCategoriaRaw(idSucursal, inicio, fin);
        List<VentasPorCategoriaDTO> ventasPorCategoria = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            String categoria = (String) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            ventasPorCategoria.add(new VentasPorCategoriaDTO(categoria, cantidad));
        }
        
        return ventasPorCategoria;
    }

    /**
     * Obtiene los métodos de pago utilizados
     */
    public List<MetodosPagoDTO> obtenerMetodosPago(Long idSucursal, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = obtenerFechaInicio(fechaInicio);
        LocalDateTime fin = obtenerFechaFin(fechaFin);
        
        List<Object[]> resultados = dashboardRepository.obtenerMetodosPagoRaw(idSucursal, inicio, fin);
        List<MetodosPagoDTO> metodosPago = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            String metodo = (String) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            metodosPago.add(new MetodosPagoDTO(metodo, cantidad));
        }
        
        return metodosPago;
    }

    /**
     * Obtiene la distribución de tallas vendidas
     */
    public List<DistribucionTallasDTO> obtenerDistribucionTallas(Long idSucursal, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = obtenerFechaInicio(fechaInicio);
        LocalDateTime fin = obtenerFechaFin(fechaFin);
        
        List<Object[]> resultados = dashboardRepository.obtenerDistribucionTallasRaw(idSucursal, inicio, fin);
        List<DistribucionTallasDTO> distribucionTallas = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            String talla = (String) resultado[0];
            Long cantidad = ((Number) resultado[1]).longValue();
            distribucionTallas.add(new DistribucionTallasDTO(talla, cantidad));
        }
        
        return distribucionTallas;
    }

    /**
     * Obtiene los productos más vendidos con su stock actual
     */
    public List<TopProductoDTO> obtenerTopProductos(Long idSucursal, LocalDate fechaInicio, LocalDate fechaFin, int limit) {
        LocalDateTime inicio = obtenerFechaInicio(fechaInicio);
        LocalDateTime fin = obtenerFechaFin(fechaFin);
        
        List<Object[]> resultados = dashboardRepository.obtenerTopProductosRaw(idSucursal, inicio, fin, limit);
        List<TopProductoDTO> topProductos = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            String nombreModelo = (String) resultado[0];
            String subtitulo = (String) resultado[1];
            Long cantidadVendida = ((Number) resultado[2]).longValue();
            String fotoUrl = (String) resultado[3];
            Integer idModelo = (Integer) resultado[4];
            
            // Obtener stock actual del modelo
            Long stockActual = dashboardRepository.obtenerStockActualPorModelo(idModelo, idSucursal);
            
            topProductos.add(new TopProductoDTO(
                    nombreModelo,
                    subtitulo,
                    cantidadVendida,
                    stockActual != null ? stockActual : 0L,
                    fotoUrl
            ));
        }
        
        return topProductos;
    }

    /**
     * Helper: Obtiene fecha inicio (si es null, usa hoy a las 00:00)
     */
    private LocalDateTime obtenerFechaInicio(LocalDate fechaInicio) {
        LocalDate fecha = (fechaInicio != null) ? fechaInicio : LocalDate.now();
        return fecha.atStartOfDay();
    }

    /**
     * Helper: Obtiene fecha fin (si es null, usa hoy a las 23:59:59)
     */
    private LocalDateTime obtenerFechaFin(LocalDate fechaFin) {
        LocalDate fecha = (fechaFin != null) ? fechaFin : LocalDate.now();
        return fecha.atTime(LocalTime.MAX);
    }
}
