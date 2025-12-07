package com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace;

import com.sistemasTarija.dunno.recepcion.application.port.out.RecepcionPersistancePort;
import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.entity.DetalleRecepcionEntity;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.entity.RecepcionEntity;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.mapper.RecepcionPersistanceMapper;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.repository.RecepcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecepcionRepositoryAdapter implements RecepcionPersistancePort {
    private final RecepcionRepository repository;
    private final RecepcionPersistanceMapper mapper;

    @Override
    public Recepcion save(Recepcion recepcion) {
        RecepcionEntity recepcionEntity = mapper.toRecepcionEntity(recepcion);
        if (recepcionEntity.getDetalles() != null) {
            for (DetalleRecepcionEntity detalle : recepcionEntity.getDetalles()) {
                detalle.setRecepcion(recepcionEntity);
            }
        }
        RecepcionEntity savedEntity = repository.save(recepcionEntity);
        return mapper.toRecepcionModel(savedEntity);
    }

    @Override
    public Optional<Recepcion> findByIdAndSucursal(Integer idRecepcion, Integer idSucursal) {
        return repository.buscarPorIdYSucursal(idRecepcion, idSucursal)
                .map(mapper::toRecepcionModel);
    }

    @Override
    public List<Recepcion> findAllByFilters(Integer idSucursal, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<RecepcionEntity> entities = repository.buscarListaConFiltros(idSucursal, fechaInicio, fechaFin);
        return mapper.toListRecepcionModel(entities);
    }
}
