package org.example.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileConversionController {

    private static final Logger logger = LoggerFactory.getLogger(FileConversionController.class);

    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> convert(
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") String format
    ) throws IOException {

        logger.info("Converting file " + file.getOriginalFilename());
        logger.info("Format: " + format);

        String originalName = file.getOriginalFilename();

        String baseName = (originalName != null && originalName.contains("."))
                ? originalName.substring(0, originalName.lastIndexOf("."))
                : "converted";

        String newFilename = baseName + "." + format.toLowerCase();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + newFilename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.getBytes());
    }
}
