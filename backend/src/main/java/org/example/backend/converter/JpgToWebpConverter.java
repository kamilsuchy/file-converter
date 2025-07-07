package org.example.backend.converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.*;

@Service
public class JpgToWebpConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {

        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Invalid image file (not an image or unsupported format)");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            boolean success = ImageIO.write(image, "webp", baos);
            if (!success) {
                throw new IOException("WebP writer not found or failed to encode image");
            }
            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return (inputFormat.equalsIgnoreCase(JPEG) || inputFormat.equalsIgnoreCase(JPG)) && targetFormat.equalsIgnoreCase(WEBP);
    }
}
