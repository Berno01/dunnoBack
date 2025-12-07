package com.sistemasTarija.dunno.recepcion.application.service;

import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ColorDTO;
import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.DetalleModeloDTO;
import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.ResumenModeloDTO;
import com.sistemasTarija.dunno.recepcion.application.dto.catalogo.TallaDTO;
import com.sistemasTarija.dunno.recepcion.application.port.in.FindModeloCatalogoUseCase;
import com.sistemasTarija.dunno.recepcion.application.port.out.ModeloCatalogoPersistancePort;
import com.sistemasTarija.dunno.recepcion.domain.exception.RecepcionFailedException;
import com.sistemasTarija.dunno.recepcion.infrastructure.adapter.out.persistenace.dto.ModeloRawDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModeloCatalogoService implements FindModeloCatalogoUseCase {
    
    private final ModeloCatalogoPersistancePort catalogoPort;

    @Override
    public List<ResumenModeloDTO> getListadoModelos() {
        return catalogoPort.obtenerListadoModelos();
    }

    @Override
    public DetalleModeloDTO getDetalleModelo(Integer idModelo) {
        List<ModeloRawDTO> rawList = catalogoPort.obtenerDetalleModeloRaw(idModelo);

        if (rawList.isEmpty()) {
            throw new RecepcionFailedException("El modelo solicitado no existe o está inactivo.");
        }

        // Extraer cabecera (datos del modelo)
        ModeloRawDTO cabecera = rawList.get(0);

        DetalleModeloDTO detalleDTO = new DetalleModeloDTO();
        detalleDTO.setIdModelo(cabecera.getIdModelo());
        detalleDTO.setNombreModelo(cabecera.getNombreModelo());
        detalleDTO.setNombreMarca(cabecera.getNombreMarca());
        detalleDTO.setNombreCategoria(cabecera.getNombreCategoria());
        detalleDTO.setNombreCorte(cabecera.getNombreCorte());

        // Agrupar variantes por color
        Map<String, List<ModeloRawDTO>> variantesPorColor = rawList.stream()
                .collect(Collectors.groupingBy(ModeloRawDTO::getNombreColor));

        List<ColorDTO> listaColores = new ArrayList<>();

        variantesPorColor.forEach((nombreColor, listaVariantesDelColor) -> {
            ModeloRawDTO infoVisualColor = listaVariantesDelColor.get(0);

            ColorDTO colorDTO = new ColorDTO();
            colorDTO.setNombreColor(nombreColor);
            colorDTO.setCodigoHex(infoVisualColor.getCodigoHex());
            colorDTO.setFotoUrl(infoVisualColor.getFotoUrl());

            // Mapear las tallas (sin stock, solo idVariante y nombre)
            List<TallaDTO> listaTallas = listaVariantesDelColor.stream()
                    .map(item -> new TallaDTO(
                            item.getIdVariante(),  // ID que se enviará al crear la recepción
                            item.getNombreTalla()  // "S", "M", "L"
                    ))
                    .collect(Collectors.toList());

            colorDTO.setTallas(listaTallas);
            listaColores.add(colorDTO);
        });

        detalleDTO.setColores(listaColores);

        return detalleDTO;
    }
}
