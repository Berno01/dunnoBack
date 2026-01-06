package com.sistemasTarija.dunno.recepcion.application.service;

import com.sistemasTarija.dunno.recepcion.application.dto.RecepcionDTO;
import com.sistemasTarija.dunno.recepcion.application.dto.RecepcionFilterDTO;
import com.sistemasTarija.dunno.recepcion.application.mapper.RecepcionMapper;
import com.sistemasTarija.dunno.recepcion.application.port.in.CreateRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.in.DeleteRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.in.FindRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.in.UpdateRecepcionUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.out.RecepcionPersistancePort;
import com.sistemasTarija.dunno.recepcion.domain.exception.RecepcionFailedException;
import com.sistemasTarija.dunno.recepcion.domain.model.DetalleRecepcion;
import com.sistemasTarija.dunno.recepcion.domain.model.Recepcion;
import com.sistemasTarija.dunno.venta.application.port.in.FindUsuarioUseCase;
import com.sistemasTarija.dunno.venta.application.service.InventarioService;
import com.sistemasTarija.dunno.venta.domain.exception.InventarioFailedExeption;
import com.sistemasTarija.dunno.venta.domain.model.Usuario;
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
public class RecepcionService implements CreateRecepcionUseCase, FindRecepcionUseCase, UpdateRecepcionUseCase, DeleteRecepcionUseCase {

    private final RecepcionPersistancePort recepcionPort;
    private final InventarioService inventarioService;
    private final FindUsuarioUseCase findUsuarioUseCase;
    private final RecepcionMapper mapper;

    @Override
    @Transactional
    public Recepcion save(RecepcionDTO recepcionDTO) {
        recepcionDTO.setEstado(true);
        recepcionDTO.setFecha(LocalDateTime.now());
        Recepcion recepcion = mapper.toDomain(recepcionDTO);

        // Guardar la recepción primero
        Recepcion savedRecepcion = recepcionPort.save(recepcion);

        // Registrar ingresos en inventario
        // La lógica de InventarioService ya maneja:
        // - Si existe (Variante+Sucursal): suma al stock existente
        // - Si NO existe: crea nuevo registro con stock inicial
        for (DetalleRecepcion detalle : savedRecepcion.getDetalles()) {
            inventarioService.registrarIngreso(
                    detalle.getIdVariante(),
                    savedRecepcion.getIdSucursal(),
                    detalle.getCantidad()
            );
        }

        return savedRecepcion;
    }

    @Override
    public Optional<RecepcionDTO> findById(Integer idRecepcion, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        Integer sucursalParaBuscar = usuario.isAdmin() ? null : usuario.getIdSucursal();

        return recepcionPort.findByIdAndSucursal(idRecepcion, sucursalParaBuscar)
                .map(mapper::toDto);
    }

    @Override
    public List<RecepcionDTO> findAll(RecepcionFilterDTO filtro, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        Integer sucursalParaBuscar;

        if (usuario.isAdmin()) {
            sucursalParaBuscar = filtro.getIdSucursal();
        } else {
            sucursalParaBuscar = usuario.getIdSucursal();
        }

        LocalDate fechaInicioBase = (filtro.getFecha() != null) ? filtro.getFecha() : LocalDate.now();
        LocalDate fechaFinBase = (filtro.getFechaFin() != null) ? filtro.getFechaFin() : LocalDate.now();
        LocalDateTime inicio = fechaInicioBase.atStartOfDay();
        LocalDateTime fin = fechaFinBase.atTime(LocalTime.MAX);

        List<Recepcion> recepcionesDominio = recepcionPort.findAllByFilters(sucursalParaBuscar, inicio, fin);
        return mapper.toDtoList(recepcionesDominio);
    }

    @Override
    @Transactional
    public Recepcion update(Integer idRecepcion, RecepcionDTO recepcionDTO, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        Integer idSucursalBusqueda = usuario.isAdmin() ? null : usuario.getIdSucursal();

        // 1. Buscar la recepción antigua
        Recepcion recepcionAntigua = recepcionPort.findByIdAndSucursal(idRecepcion, idSucursalBusqueda)
                .orElseThrow(() -> new RecepcionFailedException(
                        "Recepción no encontrada o sin permisos para editarla"));

        // 2. Validar que no esté anulada
        if (!recepcionAntigua.getEstado()) {
            throw new RecepcionFailedException("No se puede modificar una recepción anulada");
        }

        // 3. Mapear los ítems existentes para cálculo de deltas (Key: idVariante, Value: Cantidad)
        // Usamos un Map mutable para ir eliminando los procesados
        java.util.Map<Integer, Integer> mapaStockAntiguo = recepcionAntigua.getDetalles().stream()
                .collect(java.util.stream.Collectors.toMap(
                        DetalleRecepcion::getIdVariante,
                        DetalleRecepcion::getCantidad,
                        Integer::sum // Merge function por seguridad en caso de variantes duplicadas
                ));

        // 4. Mapear los nuevos datos
        Recepcion recepcionNueva = mapper.toDomain(recepcionDTO);
        recepcionNueva.setIdRecepcion(idRecepcion); // Usar el ID del path variable
        recepcionNueva.setEstado(true); // Mantener activa
        recepcionNueva.setFecha(recepcionAntigua.getFecha()); // Preservar la fecha original
        // Aseguramos mantener la sucursal original para consistencia del inventario
        recepcionNueva.setIdSucursal(recepcionAntigua.getIdSucursal());

        // 5. Calcular DELTAS y aplicar cambios al inventario
        for (DetalleRecepcion detalleNuevo : recepcionNueva.getDetalles()) {
            Integer idVariante = detalleNuevo.getIdVariante();
            Integer cantidadNueva = detalleNuevo.getCantidad();

            if (mapaStockAntiguo.containsKey(idVariante)) {
                // El ítem ya existía -> Calcular diferencia
                Integer cantidadAntigua = mapaStockAntiguo.get(idVariante);
                int delta = cantidadNueva - cantidadAntigua;

                if (delta > 0) {
                    // Aumentó la cantidad: Ingresar la diferencia
                    inventarioService.registrarIngreso(idVariante, recepcionNueva.getIdSucursal(), delta);
                } else if (delta < 0) {
                    // Disminuyó la cantidad: Revertir la diferencia (valor positivo)
                    inventarioService.revertirIngreso(idVariante, recepcionNueva.getIdSucursal(), Math.abs(delta));
                }
                // Si delta es 0, no impactamos inventario

                // Marcamos como procesado eliminándolo del mapa
                mapaStockAntiguo.remove(idVariante);
            } else {
                // Ítem es nuevo en esta actualización -> Ingresar total
                inventarioService.registrarIngreso(idVariante, recepcionNueva.getIdSucursal(), cantidadNueva);
            }
        }

        // 6. Procesar los ítems que fueron ELIMINADOS (quedaron en el mapa)
        mapaStockAntiguo.forEach((idVariante, cantidadAntigua) -> {
            inventarioService.revertirIngreso(idVariante, recepcionAntigua.getIdSucursal(), cantidadAntigua);
        });

        // 7. Guardar la recepción actualizada
        return recepcionPort.save(recepcionNueva);
    }

    @Override
    @Transactional
    public void anularRecepcion(Integer idRecepcion, Integer idUsuario) {
        Usuario usuario = findUsuarioUseCase.findById(idUsuario);
        Integer idSucursalBusqueda = usuario.isAdmin() ? null : usuario.getIdSucursal();

        Recepcion recepcion = recepcionPort.findByIdAndSucursal(idRecepcion, idSucursalBusqueda)
                .orElseThrow(() -> new RecepcionFailedException(
                        "Recepción no encontrada o sin permisos para anularla"));

        if (!recepcion.getEstado()) {
            throw new RecepcionFailedException("La recepción ya está anulada");
        }

        // Revertir ingresos del inventario
        // La lógica de InventarioService.revertirIngreso() ya valida:
        // - Que exista el registro
        // - Que no quede stock negativo
        for (DetalleRecepcion detalle : recepcion.getDetalles()) {
            inventarioService.revertirIngreso(
                    detalle.getIdVariante(),
                    recepcion.getIdSucursal(),
                    detalle.getCantidad()
            );
        }

        // Marcar como anulada
        recepcion.anular();
        recepcionPort.save(recepcion);
    }
}
