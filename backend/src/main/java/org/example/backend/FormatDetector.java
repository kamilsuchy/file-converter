package org.example.backend;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FormatDetector {

    public String findExtension(MultipartFile file) throws IOException {

        Tika tika = new Tika();

        return removeTypeIfNeeded(tika.detect(file.getInputStream()));
    }

    private String removeTypeIfNeeded(String fileName) {

        if (fileName.contains("/")) {
            return fileName.substring(fileName.lastIndexOf('/') + 1);
        }

        return fileName;
    }
}
