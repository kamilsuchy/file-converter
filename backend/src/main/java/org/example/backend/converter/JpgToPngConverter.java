package org.example.backend.converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.*;

@Service
public class JpgToPngConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {

        // Wczytaj obraz JPG
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Invalid image file");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Zapisz obraz w formacie PNG do strumienia
            boolean success = ImageIO.write(image, "png", baos);
            if (!success) {
                throw new IOException("Failed to write image as PNG");
            }
            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return (inputFormat.equalsIgnoreCase(JPEG) || inputFormat.equalsIgnoreCase(JPG)) && targetFormat.equalsIgnoreCase(PNG);
    }
}
