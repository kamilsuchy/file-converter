package org.example.backend.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.example.backend.ExtensionConstansHolder.PDF;
import static org.example.backend.ExtensionConstansHolder.TXT;

@Service
public class PdfToTxtConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Zwróć tekst jako bajty (UTF-8)
            return text.getBytes("UTF-8");
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(PDF) && targetFormat.equalsIgnoreCase(TXT);
    }
}
