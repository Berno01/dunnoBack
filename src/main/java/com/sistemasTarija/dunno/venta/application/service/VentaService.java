package com.sistemasTarija.dunno.venta.application.service;

import com.sistemasTarija.dunno.venta.application.dto.VentaFilterDTO;
import com.sistemasTarija.dunno.venta.application.port.in.*;
import com.sistemasTarija.dunno.venta.application.port.out.InventarioPersistancePort;
import com.sistemasTarija.dunno.venta.application.port.out.VentaPersistancePort;
import com.sistemasTarija.dunno.venta.domain.exception.InventarioFailedExeption;
import com.sistemasTarija.dunno.venta.domain.exception.UserFailedException;
import com.sistemasTarija.dunno.venta.domain.exception.VentaFailedException;
import com.sistemasTarija.dunno.venta.domain.model.DetalleVenta;
import com.sistemasTarija.dunno.venta.domain.model.Inventario;
import com.sistemasTarija.dunno.venta.domain.model.Usuario;
import com.sistemasTarija.dunno.venta.domain.model.Venta;
import com.sistemasTarija.dunno.venta.application.dto.VentaDTO;
import com.sistemasTarija.dunno.venta.application.mapper.VentaMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentaService implements CreateVentaUseCase, FindVentaUseCase, UpdateVentaUseCase, DeleteVentaUseCase {

    private final VentaPersistancePort ventaPort;
    private final InventarioPersistancePort inventarioPort;
    private final FindUsuarioUseCase findUsuarioUseCase;
    private final VentaMapper mapper;


    @Override
    @Transactional
    public Venta save(VentaDTO ventaDTO) {
        Venta venta = mapper.toDomain(ventaDTO);
        for(DetalleVenta detalle : venta.getDetalleVenta()) {
            Integer idVariante = detalle.getIdVariante();
            Integer idSucursal = venta.getIdSucursal();
            Optional<Inventario> optionalInventario = inventarioPort.findByIdVarianteAndIdSucursal(idVariante, idSucursal);
            Inventario inventario = optionalInventario.orElseThrow(() ->
                    new InventarioFailedExeption("La prenda con ID variante " + idVariante + " de la sucursal" + idSucursal + " no fue encontrado."));
            try{
                inventario.decreaseStock(detalle.getCantidad());
            }catch(InventarioFailedExeption e){
                throw new VentaFailedException("No se pudo completar la venta: " + e.getMessage(), e);
            }
            inventarioPort.save(inventario);
        }
        return ventaPort.save(venta);
    }

    @Override
    public Optional<VentaDTO> findById(Integer idVenta, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);

        Integer sucursalParaBuscar = usuario.isAdmin() ? null : usuario.getIdSucursal();

        return ventaPort.findByIdAndSucursal(idVenta, sucursalParaBuscar)
                .map(mapper::toDto);
    }

    @Override
    public List<VentaDTO> findAll(VentaFilterDTO filtro, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        Integer sucursalParaBuscar;

        if (usuario.isAdmin()) {
            sucursalParaBuscar = filtro.getIdSucursal();
        } else {
            sucursalParaBuscar = usuario.getIdSucursal();
        }

        LocalDate fechaBase = (filtro.getFecha() != null) ? filtro.getFecha() : LocalDate.now();
        LocalDateTime inicio = fechaBase.atStartOfDay();
        LocalDateTime fin = fechaBase.atTime(LocalTime.MAX);

        List<Venta> ventasDominio = ventaPort.findAllByFilters(sucursalParaBuscar, inicio, fin);
        return mapper.toDtoList(ventasDominio);
    }

    @Override
    @Transactional
    public VentaDTO update(Integer idVenta, VentaDTO ventaDTO, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        Integer idSucursalBusqueda = usuario.isAdmin() ? null : usuario.getIdSucursal();

        Venta ventaAntigua = ventaPort.findByIdAndSucursal(idVenta, idSucursalBusqueda)
                .orElseThrow(() -> new VentaFailedException("Venta no encontrada o sin permisos para editarla"));

        for (DetalleVenta detalleViejo : ventaAntigua.getDetalleVenta()) {

            Inventario inventario = inventarioPort.findByIdVarianteAndIdSucursal(detalleViejo.getIdVariante(), ventaAntigua.getIdSucursal())
                    .orElseThrow(() -> new InventarioFailedExeption("Error de integridad: Inventario no encontrado para devolución."));
            // DEVOLVEMOS al stock
            inventario.increaseStock(detalleViejo.getCantidad());
            inventarioPort.save(inventario);
        }
        Venta ventaNuevaDatos = mapper.toDomain(ventaDTO);
        for (DetalleVenta detalleNuevo : ventaNuevaDatos.getDetalleVenta()) {

            Inventario inventario = inventarioPort.findByIdVarianteAndIdSucursal(detalleNuevo.getIdVariante(), ventaAntigua.getIdSucursal())
                    .orElseThrow(() -> new InventarioFailedExeption("Inventario no encontrado para el nuevo item ID: " + detalleNuevo.getIdVariante()));

            try {
                // DESCONTAMOS del stock
                inventario.decreaseStock(detalleNuevo.getCantidad());
            } catch (InventarioFailedExeption e) {
                throw new VentaFailedException("No se puede actualizar: Stock insuficiente para el item " + detalleNuevo.getIdVariante(), e);
            }
            inventarioPort.save(inventario);
        }

        ventaAntigua.actualizarDatos(
                ventaNuevaDatos.getDetalleVenta(),
                ventaNuevaDatos.getMontoEfectivo(),
                ventaNuevaDatos.getMontoQr(),
                ventaNuevaDatos.getMontoTarjeta(),
                ventaNuevaDatos.getTipoVenta()
        );

        Venta ventaActualizada = ventaPort.save(ventaAntigua);
        return mapper.toDto(ventaActualizada);
    }

    @Override
    @Transactional
    public void delete(Integer idVenta, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        if (!usuario.isAdmin()) {
            throw new UserFailedException("Permiso denegado: Solo los administradores pueden anular ventas.");
        }
        Venta venta = ventaPort.findByIdAndSucursal(idVenta, null)
                .orElseThrow(() -> new VentaFailedException("La venta con ID " + idVenta + " no existe."));


        if (!venta.getEstadoVenta()) {
            throw new VentaFailedException("Esta venta ya fue anulada anteriormente.");
        }

        for (DetalleVenta detalle : venta.getDetalleVenta()) {
            Inventario inventario = inventarioPort.findByIdVarianteAndIdSucursal(
                    detalle.getIdVariante(),
                    venta.getIdSucursal()
            ).orElseThrow(() -> new InventarioFailedExeption("Error: No se encontró el inventario para devolver el item " + detalle.getIdVariante()));
            inventario.increaseStock(detalle.getCantidad());
            inventarioPort.save(inventario);
        }
        venta.setEstadoVenta(false);
        ventaPort.save(venta);
    }

    @Override
    @Transactional
    public void activate(Integer idVenta, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        if (!usuario.isAdmin()) {
            throw new UserFailedException("Permiso denegado: Solo los administradores pueden reactivar ventas.");
        }

        Venta venta = ventaPort.findByIdAndSucursal(idVenta, null)
                .orElseThrow(() -> new VentaFailedException("La venta con ID " + idVenta + " no existe."));

        if (venta.getEstadoVenta()) {
            throw new VentaFailedException("Esta venta ya se encuentra activa.");
        }
        for (DetalleVenta detalle : venta.getDetalleVenta()) {

            Inventario inventario = inventarioPort.findByIdVarianteAndIdSucursal(
                    detalle.getIdVariante(),
                    venta.getIdSucursal()
            ).orElseThrow(() -> new InventarioFailedExeption("Inventario no encontrado para el item " + detalle.getIdVariante()));
            try {
                inventario.decreaseStock(detalle.getCantidad());
            } catch (Exception e) {
                throw new VentaFailedException(
                        "No se puede reactivar la venta: Stock insuficiente para el item " + detalle.getIdVariante() +
                                ". Stock actual: " + inventario.getStock() + ", Requerido: " + detalle.getCantidad()
                );
            }
            inventarioPort.save(inventario);
        }
        venta.setEstadoVenta(true);
        ventaPort.save(venta);
    }
}
