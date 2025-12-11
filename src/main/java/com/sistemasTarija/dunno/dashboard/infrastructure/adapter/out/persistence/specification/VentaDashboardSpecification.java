package com.sistemasTarija.dunno.dashboard.infrastructure.adapter.out.persistence.specification;

import com.sistemasTarija.dunno.dashboard.application.dto.DashboardFilterDTO;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.VentaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifications para construir predicados dinámicos de filtrado de ventas
 * para el dashboard. Permite combinar múltiples filtros de forma eficiente.
 */
public class VentaDashboardSpecification {

    /**
     * Construye el predicado base que SIEMPRE se aplica:
     * - Estado activo (estado_venta = true)
     * - Rango de fechas (si se proporciona)
     * - Vendedor específico (si se proporciona)
     * - Sucursal (si se proporciona - para usuarios no admin)
     *
     * @param filter DTO con los filtros a aplicar
     * @return Specification con todos los predicados aplicables
     */
    public static Specification<VentaEntity> buildBaseFilter(DashboardFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // FILTRO OBLIGATORIO: Solo ventas activas
            predicates.add(criteriaBuilder.isTrue(root.get("estadoVenta")));

            // FILTRO DE FECHAS: Si se proporcionan fechas, aplicar rango
            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                LocalDateTime startDateTime = filter.getStartDate().atStartOfDay();
                LocalDateTime endDateTime = filter.getEndDate().atTime(LocalTime.MAX);
                
                predicates.add(criteriaBuilder.between(
                    root.get("fecha"),
                    startDateTime,
                    endDateTime
                ));
            } else if (filter.getStartDate() != null) {
                // Solo fecha de inicio
                LocalDateTime startDateTime = filter.getStartDate().atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("fecha"),
                    startDateTime
                ));
            } else if (filter.getEndDate() != null) {
                // Solo fecha de fin
                LocalDateTime endDateTime = filter.getEndDate().atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("fecha"),
                    endDateTime
                ));
            }

            // FILTRO INTERACTIVO DE VENDEDOR: Si se seleccionó un vendedor específico
            if (filter.getSalesRepId() != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("createdBy"),
                    filter.getSalesRepId()
                ));
            }

            // FILTRO DE SUCURSAL: Para usuarios no admin
            if (filter.getIdSucursal() != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("idSucursal"),
                    filter.getIdSucursal()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Specification para filtrar solo ventas con descuento
     */
    public static Specification<VentaEntity> hasDescuento() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("descuento"), 0.0);
    }

    /**
     * Combina el filtro base con el filtro de descuento
     */
    public static Specification<VentaEntity> buildFilterWithDescuento(DashboardFilterDTO filter) {
        return Specification.where(buildBaseFilter(filter)).and(hasDescuento());
    }
}
