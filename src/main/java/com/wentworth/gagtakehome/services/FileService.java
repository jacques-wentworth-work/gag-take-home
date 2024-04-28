package com.wentworth.gagtakehome.services;

import com.wentworth.gagtakehome.services.dto.TransportDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<TransportDTO> parseFile(MultipartFile file, Boolean skipValidation);
}
