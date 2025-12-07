package com.sistemasTarija.dunno.venta.application.port.in;

import com.sistemasTarija.dunno.venta.application.dto.catalogo.DetallePrendaDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;

import java.util.List;

public interface FindCatalogoUseCase {

    List<ResumenPrendaDTO> getListadoGeneral(Integer idSucursal);
    DetallePrendaDTO getDetalleModelo(Integer idModelo, Integer idSucursal);
}
