package com.sistemasTarija.dunno.venta.application.service;

import com.sistemasTarija.dunno.venta.application.dto.catalogo.ColorDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.DetallePrendaDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.ResumenPrendaDTO;
import com.sistemasTarija.dunno.venta.application.dto.catalogo.TallaDTO;
import com.sistemasTarija.dunno.venta.application.port.in.FindCatalogoUseCase;
import com.sistemasTarija.dunno.venta.application.port.out.InventarioCatalogoVentaPersistancePort;
import com.sistemasTarija.dunno.venta.domain.exception.InventarioFailedExeption;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.dto.InventarioRawDTO;
import com.sistemasTarija.dunno.venta.infrastructure.adapter.out.persistenace.repository.InventarioVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService implements FindCatalogoUseCase {
    private final InventarioCatalogoVentaPersistancePort catalogoPort;


    @Override
    public List<ResumenPrendaDTO> getListadoGeneral(Integer idSucursal) {
        return catalogoPort.obtenerListadoResumen(idSucursal);
    }

    @Override
    public DetallePrendaDTO getDetalleModelo(Integer idModelo, Integer idSucursal) {
        List<InventarioRawDTO> rawList = catalogoPort.obtenerDetalleModeloRaw(idSucursal, idModelo);

        if (rawList.isEmpty()) {
            throw new InventarioFailedExeption("El modelo solicitado no existe o no tiene stock disponible en esta sucursal.");
        }

        // 2. Extraer cabecera (Tomamos el primer registro porque los datos del modelo se repiten en todos)
        InventarioRawDTO cabecera = rawList.get(0);

        DetallePrendaDTO detalleDTO = new DetallePrendaDTO();
        detalleDTO.setIdModelo(cabecera.getIdModelo());
        detalleDTO.setNombreModelo(cabecera.getNombreModelo());
        detalleDTO.setNombreMarca(cabecera.getNombreMarca());
        detalleDTO.setNombreCategoria(cabecera.getNombreCategoria());
        detalleDTO.setNombreCorte(cabecera.getNombreCorte());


        Map<String, List<InventarioRawDTO>> variantesPorColor = rawList.stream()
                .collect(Collectors.groupingBy(InventarioRawDTO::getNombreColor));

        List<ColorDTO> listaColores = new ArrayList<>();

        variantesPorColor.forEach((nombreColor, listaVariantesDelColor) -> {

            InventarioRawDTO infoVisualColor = listaVariantesDelColor.get(0);

            ColorDTO colorDTO = new ColorDTO();
            colorDTO.setNombreColor(nombreColor);
            colorDTO.setCodigoHex(infoVisualColor.getCodigoHex());
            colorDTO.setFotoUrl(infoVisualColor.getFotoUrl());


            List<TallaDTO> listaTallas = listaVariantesDelColor.stream()
                    .map(item -> new TallaDTO(
                            item.getIdVariante(),  // Este es el ID que el front mandará al vender
                            item.getNombreTalla(), // "S", "M", "L"
                            item.getStock()        // "40", "15"
                    ))
                    .collect(Collectors.toList());

            colorDTO.setTallas(listaTallas);

            listaColores.add(colorDTO);
        });
        detalleDTO.setColores(listaColores);

        return detalleDTO;
    }
}
