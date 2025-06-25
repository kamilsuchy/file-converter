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
public class PngToWebpConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {
        // Wczytaj PNG jako BufferedImage
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Invalid PNG image");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Zapisz obraz jako WebP
            boolean success = ImageIO.write(image, "webp", baos);
            if (!success) {
                throw new IOException("Failed to write image as WebP");
            }
            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(PNG) && targetFormat.equalsIgnoreCase(WEBP);
    }
}
