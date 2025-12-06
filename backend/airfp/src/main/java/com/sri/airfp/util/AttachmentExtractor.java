package com.sri.airfp.util;



import com.sri.airfp.model.AttachmentData;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class AttachmentExtractor {

    private final Tika tika = new Tika();


    public AttachmentData extract(String fileName, InputStream is, long size, Path tempDir) throws Exception {
        String safeName = fileName == null ? "attachment" : fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path tmp = Files.createTempFile(tempDir, "att-", "-" + safeName);
        tmp.toFile().deleteOnExit();
        try (var out = Files.newOutputStream(tmp)) {
            is.transferTo(out);
        }

        Metadata metadata = new Metadata();

        metadata.set("resourceName", safeName);


        String detected = tika.detect(tmp.toFile());
        if (detected != null && !detected.isBlank()) {
            metadata.set(Metadata.CONTENT_TYPE, detected);
        }

        String text;
        try (var in = Files.newInputStream(tmp)) {

            text = tika.parseToString(in, metadata);
        }

        AttachmentData ad = new AttachmentData();

        ad.setFilename(safeName);
        ad.setMimeType ( metadata.get(Metadata.CONTENT_TYPE));
        ad.setSize(size);
        ad.setExtractedText(text == null ? "" : text);
        ad.setLocalPath(tmp.toAbsolutePath().toString());
        return ad;
    }
}

