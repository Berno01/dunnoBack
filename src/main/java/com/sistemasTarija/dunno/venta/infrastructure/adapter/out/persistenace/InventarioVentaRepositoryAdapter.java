package com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace;

import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;
import com.sistemasTarija.dunno.venta.application.port.out.InventarioCatalogoVentaPersistancePort;
import com.sistemasTarija.dunno.venta.application.port.out.InventarioPersistancePort;
import com.sistemasTarija.dunno.venta.domain.model.Inventario;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto.InventarioRawDTO;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.mapper.InventarioVentaPersistanceMapper;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository.InventarioVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InventarioVentaRepositoryAdapter implements InventarioPersistancePort, InventarioCatalogoVentaPersistancePort {
    private final InventarioVentaPersistanceMapper mapper;
    private final InventarioVentaRepository repository;

    @Override
    public Optional<Inventario> findById(Integer idInventario) {
        return Optional.empty();
    }

    @Override
    public Inventario save(Inventario inventario) {
        return mapper.toInventarioDomain(repository.save(mapper.toInventarioEntity(inventario)));
    }

    @Override
    public List<Inventario> findAll() {
        return mapper.toInventarioList(repository.findAllByEstadoTrue());
    }

    @Override
    public Optional<Inventario> findByIdVarianteAndIdSucursal(Integer idVariante, Integer idSucursal) {
        return repository.findByIdVarianteAndIdSucursal(idVariante,idSucursal).map(mapper::toInventarioDomain);
    }

    @Override
    public List<InventarioRawDTO> obtenerDetalleModeloRaw(Integer idSucursal, Integer idModelo) {
        return repository.obtenerDetalleModeloRaw(idSucursal, idModelo);
    }

    @Override
    public List<ResumenPrendaDTO> obtenerListadoResumen(Integer idSucursal) {
        return repository.obtenerListadoResumen(idSucursal);
    }
}
