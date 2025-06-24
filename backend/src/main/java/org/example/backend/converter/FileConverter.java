package org.example.backend.converter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileConverter {

    byte[] convert(MultipartFile file) throws IOException;
}
