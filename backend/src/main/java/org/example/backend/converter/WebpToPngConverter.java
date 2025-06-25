package org.example.backend.converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.PNG;
import static org.example.backend.ExtensionConstansHolder.WEBP;

@Service
public class WebpToPngConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {
        // Wczytaj WebP jako BufferedImage
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Invalid WebP image");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Zapisz jako PNG
            boolean success = ImageIO.write(image, "png", baos);
            if (!success) {
                throw new IOException("Failed to write image as PNG");
            }
            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(WEBP) && targetFormat.equalsIgnoreCase(PNG);
    }
}
