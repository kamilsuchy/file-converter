package org.example.backend.converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static org.example.backend.ExtensionConstansHolder.PNG;
import static org.example.backend.ExtensionConstansHolder.WEBP;

@Service
public class PngToWebpConverter implements FileConverter {

    @Override
    public byte[] convert(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("Invalid image input");
        }

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("webp");
        if (!writers.hasNext()) {
            throw new IOException("No WebP writer found (is TwelveMonkeys on classpath?)");
        }

        ImageWriter writer = writers.next();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {

            writer.setOutput(ios);
            writer.write(image);
            writer.dispose();

            return baos.toByteArray();
        }
    }

    @Override
    public boolean isApplicable(String inputFormat, String targetFormat) {

        return inputFormat.equalsIgnoreCase(PNG) && targetFormat.equalsIgnoreCase(WEBP);
    }
}
