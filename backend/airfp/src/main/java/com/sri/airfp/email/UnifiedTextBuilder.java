package com.sri.airfp.email;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.airfp.model.AttachmentData;
import com.sri.airfp.model.EmailDocument;
import com.sri.airfp.model.Table;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnifiedTextBuilder {

    private final ObjectMapper mapper = new ObjectMapper();

    public String build(EmailDocument doc) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("Email Subject: ").append(nullSafe(doc.subject)).append("\n");
        sb.append("From: ").append(nullSafe(doc.from)).append("\n");
        sb.append("To: ").append(String.join(", ", doc.to == null ? List.of() : doc.to)).append("\n");
        sb.append("ReceivedDate: ").append(doc.receivedDate == null ? "" : doc.receivedDate.toString()).append("\n\n");

        if (doc.plainText != null && !doc.plainText.isBlank()) {
            sb.append("Email Body (plain text):\n");
            sb.append(snippet(doc.plainText, 4000)).append("\n\n");
        }

        if (doc.htmlText != null && !doc.htmlText.isBlank()) {
            sb.append("Email Body (cleaned HTML converted to text):\n");
            sb.append(snippet(doc.htmlText, 4000)).append("\n\n");
        }

        if (doc.tables != null && !doc.tables.isEmpty()) {
            sb.append("Tables:\n");
            for (Table t : doc.tables) {
                sb.append(mapper.writeValueAsString(t)).append("\n");
            }
            sb.append("\n");
        }

        if (doc.attachments != null && !doc.attachments.isEmpty()) {
            sb.append("Attachments:\n");
            for (AttachmentData a : doc.attachments) {
                sb.append("Filename: ").append(a.getFilename()).append(", size: ").append(a.getSize()).append("\n");
                sb.append("Attachment Text (first 3000 chars):\n");
                sb.append(snippet(a.getExtractedText(), 3000)).append("\n\n");
            }
        }

        // small instruction header to LLM
        sb.append("\n\n---- End of Document ----\n");
        return sb.toString();
    }

    private String nullSafe(String s) { return s == null ? "" : s; }
    private String snippet(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max) + "\n... [TRUNCATED]";
    }
}
