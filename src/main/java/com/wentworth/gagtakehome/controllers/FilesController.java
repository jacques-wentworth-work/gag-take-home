package com.wentworth.gagtakehome.controllers;

import com.wentworth.gagtakehome.controllers.resources.TransportResource;
import com.wentworth.gagtakehome.services.FileService;
import com.wentworth.gagtakehome.services.dto.TransportDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.wentworth.gagtakehome.controllers.assemblers.FileAssembler.assemble;

@RestController
@RequestMapping(value = "/files")
public class FilesController {

    private final FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "ping")
    public String ping(@RequestHeader("X-request-id") String requestId,
                       HttpServletResponse response) {
        response.addHeader("X-request-id", requestId);
        return "pong";
    }

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<TransportResource>> parsedFile(@RequestParam(value = "file") MultipartFile file,
                                                              @RequestParam(value = "skipValidation", required = false) Boolean skipValidation,
                                                              @RequestHeader("X-request-id") String requestId,
                                                              HttpServletResponse response) {
        response.addHeader("X-request-id", requestId);

        return ResponseEntity.ok(assemble(fileService.parseFile(file, skipValidation)));
    }
}
