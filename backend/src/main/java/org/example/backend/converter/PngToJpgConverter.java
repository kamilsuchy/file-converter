package org.example.backend.converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.*;

@Service
public class PngToJpgConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {
        // Wczytaj PNG jako BufferedImage
        BufferedImage pngImage = ImageIO.read(file.getInputStream());

        if (pngImage == null) {
            throw new IOException("Invalid PNG image");
        }

        // Utwórz obraz RGB bez alfa (JPEG nie obsługuje przezroczystości)
        BufferedImage jpgImage = new BufferedImage(
                pngImage.getWidth(),
                pngImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // Wypełnij tło białym (albo innym kolorem)
        Graphics2D g = jpgImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, jpgImage.getWidth(), jpgImage.getHeight());
        // Narysuj oryginalny obraz na tle
        g.drawImage(pngImage, 0, 0, null);
        g.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            boolean success = ImageIO.write(jpgImage, "jpg", baos);
            if (!success) {
                throw new IOException("Failed to write image as JPG");
            }
            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(PNG) && (targetFormat.equalsIgnoreCase(JPG) || targetFormat.equalsIgnoreCase(JPEG));
    }
}
