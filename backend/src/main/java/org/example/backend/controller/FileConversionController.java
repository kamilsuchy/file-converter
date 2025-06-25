package org.example.backend.controller;

import org.example.backend.FormatDetector;
import org.example.backend.converter.FileConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class FileConversionController {

    private static final Logger logger = LoggerFactory.getLogger(FileConversionController.class);

    private final List<FileConverter> converters;
    private final FormatDetector formatDetector;


    @Autowired
    FileConversionController(List<FileConverter> converters,
                             FormatDetector formatDetector) {

        this.converters = converters;
        this.formatDetector = formatDetector;
    }

    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> convert(
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") String format
    ) throws IOException {

        logger.info("Converting file " + file.getOriginalFilename());
        logger.info("Format: " + format);

        byte[] contentConverted = convertFile(file, format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + findNewFileName(file, format))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(contentConverted);
    }

    private String findNewFileName(MultipartFile file, String targetFormat) {

        String originalName = file.getOriginalFilename();

        String baseName = (originalName != null && originalName.contains("."))
                ? originalName.substring(0, originalName.lastIndexOf("."))
                : "converted";

        return baseName + "." + targetFormat.toLowerCase();
    }

    private byte[] convertFile(MultipartFile file, String targetFormat) throws IOException {

        return converter(formatDetector.findExtension(file), targetFormat)
                .convert(file);
    }

    private FileConverter converter(String inputFormat, String targetFormat) {

        return converters.stream()
                .filter(converter -> converter.isApplicable(inputFormat, targetFormat))
                .findFirst()
                .orElseThrow();
    }
}
