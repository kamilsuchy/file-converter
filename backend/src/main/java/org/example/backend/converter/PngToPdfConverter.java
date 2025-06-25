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

import static org.example.backend.ExtensionConstansHolder.PDF;
import static org.example.backend.ExtensionConstansHolder.PNG;

@Service
public class PngToPdfConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, file.getBytes(), file.getOriginalFilename());

            PDRectangle rect = new PDRectangle(pdImage.getWidth(), pdImage.getHeight());
            PDPage page = new PDPage(rect);
            doc.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                contentStream.drawImage(pdImage, 0, 0, pdImage.getWidth(), pdImage.getHeight());
            }

            doc.save(out);
            return out.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(PNG) && targetFormat.equalsIgnoreCase(PDF);
    }
}
