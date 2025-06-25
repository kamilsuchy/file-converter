package org.example.backend.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.example.backend.ExtensionConstansHolder.PDF;
import static org.example.backend.ExtensionConstansHolder.TXT;

@Service
public class TxtToPdfConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {

        try (PDDocument doc = new PDDocument();
             BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 700);

                String line;
                while ((line = reader.readLine()) != null) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }

                contentStream.endText();
            }

            // Zapisz do tablicy bajtów
            try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
                doc.save(baos);
                return baos.toByteArray();
            }
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(TXT) && targetFormat.equalsIgnoreCase(PDF);
    }
}
