package org.example.backend.converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.JPG;
import static org.example.backend.ExtensionConstansHolder.WEBP;

@Service
public class JpgToWebpConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {

        // Wczytaj JPG jako BufferedImage
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Invalid image file");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Zapisz jako WebP
            boolean success = ImageIO.write(image, "webp", baos);
            if (!success) {
                throw new IOException("Failed to write image in WebP format");
            }
            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(JPG) && targetFormat.equals(WEBP);
    }
}
