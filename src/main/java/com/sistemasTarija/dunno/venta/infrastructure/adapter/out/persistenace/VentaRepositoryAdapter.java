package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace;

import com.sistemasTarija.dunno.venta.application.port.out.VentaPersistancePort;
import com.sistemasTarija.dunno.venta.domain.model.Venta;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.DetalleVentaEntity;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.entity.VentaEntity;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.mapper.VentaPersistanceMapper;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VentaRepositoryAdapter implements VentaPersistancePort {
    private final VentaRepository repository;
    private final VentaPersistanceMapper mapper;


    @Override
    public Venta save(Venta venta) {
        VentaEntity ventaEntity = mapper.toVentaEntity(venta);
        if (ventaEntity.getDetalleVenta() != null) {
            for (DetalleVentaEntity detalles: ventaEntity.getDetalleVenta()) {
                detalles.setVenta(ventaEntity);
            }
        }
        VentaEntity savedEntity = repository.save(ventaEntity);
        return mapper.toVentaModel(savedEntity);
    }

    @Override
    public Optional<Venta> findByIdAndSucursal(Integer idVenta, Integer idSucursal) {
        return repository.buscarPorIdYSucursal(idVenta, idSucursal)
                .map(mapper::toVentaModel);
    }
    @Override
    public List<Venta> findAllByFilters(Integer idSucursal, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<VentaEntity> entities = repository.buscarListaConFiltros(idSucursal, fechaInicio, fechaFin);
        return mapper.toListVentaModel(entities);
    }


}
