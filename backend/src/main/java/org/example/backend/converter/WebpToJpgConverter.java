package org.example.backend.converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.*;

@Service
public class WebpToJpgConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {
        // Wczytaj WebP jako BufferedImage
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Invalid WebP image");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Zapisz jako JPG
            boolean success = ImageIO.write(image, "jpg", baos);
            if (!success) {
                throw new IOException("Failed to write image as JPG");
            }
            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(WEBP) && (targetFormat.equalsIgnoreCase(JPG) || targetFormat.equalsIgnoreCase(JPEG));
    }
}
