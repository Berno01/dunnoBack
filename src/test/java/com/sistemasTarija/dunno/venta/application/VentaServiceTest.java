package com.sistemasTarija.dunno.venta.application;


import com.sistemasTarija.dunno.venta.application.dto.DetalleVentaDTO;
import com.sistemasTarija.dunno.venta.application.mapper.VentaMapper;
import com.sistemasTarija.dunno.venta.application.port.out.InventarioPersistancePort;
import com.sistemasTarija.dunno.venta.application.port.out.VentaPersistancePort;
import com.sistemasTarija.dunno.venta.application.service.VentaService;
import com.sistemasTarija.dunno.venta.domain.model.DetalleVenta;
import com.sistemasTarija.dunno.venta.domain.model.Inventario;
import com.sistemasTarija.dunno.venta.application.dto.VentaDTO;
import com.sistemasTarija.dunno.venta.domain.model.Venta;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.mapper.VentaPersistanceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {
    @Mock
    private InventarioPersistancePort inventarioPersistancePort;
    @Mock
    private VentaPersistancePort ventaPersistancePort;
    @Mock
    private VentaMapper ventaMapper;
    @InjectMocks
    private VentaService ventaService;

    @Test
    void deberiaGuardarVentaConStock(){
        // 1. ARRANGE (PREPARAR DATOS)

        Integer idVariante = 1;
        Integer cantidad = 5;
        Double precioUnitario = 50.00;
        Double totalDetalle = 250.00;

        Integer idSucursal = 1;
        String fechaHora = "2025-10-15 23:36:20";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaHoraObj = LocalDateTime.parse(fechaHora, formatter);
        Double montoEfectivo=230.00;
        Double montoQr=0.00;
        Double montoTarjeta=20.00;
        Double montoGiftcard=0.00;
        Double descuento=0.00;
        String tipoDescuento="SIN DESCUENTO";
        String tipo="Local";
        Double totalVenta=montoEfectivo+montoQr+montoTarjeta;
        Integer idUsuario = 1;


        DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
        detalleDTO.setIdVariante(idVariante);
        detalleDTO.setCantidad(cantidad);
        detalleDTO.setPrecioUnitario(precioUnitario);
        detalleDTO.setTotal(totalDetalle);


        VentaDTO ventaDeEntrada = new VentaDTO(null,fechaHoraObj,idSucursal,totalVenta,montoEfectivo, montoQr, montoTarjeta, montoGiftcard, descuento, tipoDescuento, tipo, idUsuario, null, List.of(detalleDTO));


        DetalleVenta detalleDominio = new DetalleVenta(idVariante, cantidad, precioUnitario);

        Venta ventaDominio = new Venta(1,fechaHoraObj,idSucursal,montoEfectivo,montoQr,montoTarjeta, montoGiftcard, descuento, tipoDescuento, tipo, List.of(detalleDominio));
        ventaDominio.setDetalleVenta(List.of(detalleDominio));

        Inventario inventarioExistente = new Inventario(1, idVariante, 50, idSucursal);

        // 2. MOCKING (DEFINIR COMPORTAMIENTO)

        // Cuando el servicio llame al mapper, devolvemos el objeto de dominio preparado
        when(ventaMapper.toDomain(any())).thenReturn(ventaDominio);

        // Cuando busque inventario, devolvemos el que tiene 50 de stock
        when(inventarioPersistancePort.findByIdVarianteAndIdSucursal(idVariante, idSucursal))
                .thenReturn(Optional.of(inventarioExistente));

        // Cuando guarde la venta, devolvemos la misma venta (simulando que la DB la guardó y retornó con ID)

        when(ventaPersistancePort.save(any(Venta.class))).thenReturn(ventaDominio);


        // 3. ACT (EJECUTAR)

        Venta resultado = ventaService.save(ventaDeEntrada);


        // 4. ASSERT (VERIFICAR)

        assertNotNull(resultado);

        // Verificamos Lógica de Negocio: ¿Se descontó el stock?
        // Tenías 50, compraste 5 -> Debería quedar 45.
        assertEquals(45, inventarioExistente.getStock(), "El stock debió bajar de 50 a 45");

        // Verificamos interacciones
        verify(ventaMapper).toDomain(any()); // Se llamó al mapper
        verify(inventarioPersistancePort).findByIdVarianteAndIdSucursal(idVariante, idSucursal); // Se buscó stock
        verify(inventarioPersistancePort).save(inventarioExistente); // Se guardó el inventario actualizado
        verify(ventaPersistancePort).save(ventaDominio); // Se guardó la venta de dominio
    }


}
