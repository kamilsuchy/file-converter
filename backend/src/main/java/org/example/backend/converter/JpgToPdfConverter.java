package org.example.backend.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.*;

@Service
public class JpgToPdfConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {

        try (PDDocument doc = new PDDocument();

             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Załaduj obraz JPG z pliku
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, file.getBytes(), file.getOriginalFilename());

            // Stwórz nową stronę PDF o wymiarach obrazu
            PDRectangle rect = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
            PDPage page = new PDPage(rect);
            doc.addPage(page);

            // Dodaj obraz do strony PDF
            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
            }

            // Zapisz PDF do output stream
            doc.save(out);

            return out.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return (inputFormat.equalsIgnoreCase(JPEG) || inputFormat.equalsIgnoreCase(JPG)) && targetFormat.equalsIgnoreCase(PDF);
    }
}
