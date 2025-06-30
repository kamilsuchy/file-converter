package org.example.backend.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.PDF;
import static org.example.backend.ExtensionConstansHolder.WEBP;

@Service
public class WebpToPdfConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {
        // Wczytaj WebP jako BufferedImage (dzięki imageio-webp)
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IOException("Invalid WebP image");
        }

        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
            doc.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, file.getBytes(), file.getOriginalFilename());

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                contentStream.drawImage(pdImage, 0, 0, image.getWidth(), image.getHeight());
            }

            doc.save(out);
            return out.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(WEBP) && targetFormat.equalsIgnoreCase(PDF);
    }
}
