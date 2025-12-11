package com.sistemasTarija.dunno.venta.application.mapper;

import com.sistemasTarija.dunno.venta.application.dto.DetalleVentaDTO;
import com.sistemasTarija.dunno.venta.application.dto.VentaDTO;
import com.sistemasTarija.dunno.venta.domain.model.DetalleVenta;
import com.sistemasTarija.dunno.venta.domain.model.Venta;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VentaMapper {
    public Venta toDomain(VentaDTO dto) {
        List<DetalleVenta> detalles = dto.getDetalleVenta().stream()
                .map(this::toDetalleVentaDomain)
                .collect(Collectors.toList());

        return new Venta(
                dto.getIdVenta(),
                LocalDateTime.now(), // Se genera automáticamente la fecha
                dto.getIdSucursal(),
                dto.getMontoEfectivo(),
                dto.getMontoQr(),
                dto.getMontoTarjeta(),
                dto.getMontoGiftcard(),
                dto.getDescuento(),
                dto.getTipoDescuento(),
                dto.getTipo(),
                detalles
        );
    }

    private DetalleVenta toDetalleVentaDomain(DetalleVentaDTO dto) {
        return new DetalleVenta(
                dto.getIdVariante(),
                dto.getCantidad(),
                dto.getPrecioUnitario()
        );
    }

    public VentaDTO toDto(Venta domain) {
        // Convertimos los detalles
        List<DetalleVentaDTO> detallesDto = domain.getDetalleVenta().stream()
                .map(this::toDetalleVentaDto)
                .collect(Collectors.toList());

        return new VentaDTO(
                domain.getIdVenta(),
                domain.getFecha(),
                domain.getIdSucursal(),
                domain.getTotal(),
                domain.getMontoEfectivo(),
                domain.getMontoQr(),
                domain.getMontoTarjeta(),
                domain.getMontoGiftcard(),
                domain.getDescuento(),
                domain.getTipoDescuento(),
                domain.getTipoVenta(),
                domain.getCreatedBy(),
                null, // username - se setea en el servicio
                detallesDto
        );
    }

    public VentaDTO toDtoWithoutDetails(Venta domain) {
        return new VentaDTO(
                domain.getIdVenta(),
                domain.getFecha(),
                domain.getIdSucursal(),
                domain.getTotal(),
                domain.getMontoEfectivo(),
                domain.getMontoQr(),
                domain.getMontoTarjeta(),
                domain.getMontoGiftcard(),
                domain.getDescuento(),
                domain.getTipoDescuento(),
                domain.getTipoVenta(),
                domain.getCreatedBy(),
                null, // username - se setea en el servicio
                null // Sin detalle de venta para listados
        );
    }

    private DetalleVentaDTO toDetalleVentaDto(DetalleVenta domain) {
        DetalleVentaDTO dto = new DetalleVentaDTO(
                domain.getIdVariante(),
                domain.getCantidad(),
                domain.getPrecioUnitario(),
                domain.getTotal()
        );
        // Agregar idModelo para respuesta al frontend
        dto.setIdModelo(domain.getIdModelo());
        return dto;
    }

    // Helper para listas
    public List<VentaDTO> toDtoList(List<Venta> domainList) {
        return domainList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
