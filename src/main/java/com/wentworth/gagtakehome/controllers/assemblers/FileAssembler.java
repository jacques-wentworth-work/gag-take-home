package com.wentworth.gagtakehome.controllers.assemblers;

import com.wentworth.gagtakehome.controllers.resources.TransportResource;
import com.wentworth.gagtakehome.services.dto.TransportDTO;

import java.util.List;

public class FileAssembler {

    public static List<TransportResource> assemble(List<TransportDTO> dtos) {
        return dtos.stream()
                .map(dto -> new TransportResource(dto.name(), dto.transport(), dto.topSpeed()))
                .toList();

    }
}
