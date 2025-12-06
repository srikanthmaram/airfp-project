package com.sri.airfp.email;


import com.sri.airfp.model.AttachmentData;
import com.sri.airfp.model.EmailDocument;
import com.sri.airfp.util.AttachmentExtractor;
import com.sri.airfp.util.HtmlCleaner;
import com.sri.airfp.util.TableExtractor;
import jakarta.mail.*;
import jakarta.mail.BodyPart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Service
public class EmailNormalizationService {

    private final AttachmentExtractor attachmentExtractor;
    private final TableExtractor tableExtractor;
    private final HtmlCleaner htmlCleaner;

    @Value("${app.temp-dir:/tmp}")
    private String tempDir;

    public EmailNormalizationService(AttachmentExtractor attachmentExtractor,
                                     TableExtractor tableExtractor,
                                     HtmlCleaner htmlCleaner) {
        this.attachmentExtractor = attachmentExtractor;
        this.tableExtractor = tableExtractor;
        this.htmlCleaner = htmlCleaner;
    }

    public EmailDocument normalizeMessage(Message message) throws Exception {
        EmailDocument doc = new EmailDocument();
        doc.subject = message.getSubject();
        Address[] from = message.getFrom();
        doc.from = from != null && from.length>0 ? from[0].toString() : null;
        doc.receivedDate = message.getReceivedDate();
        doc.messageId = message.getHeader("Message-ID") != null ? message.getHeader("Message-ID")[0] : null;

        doc.to = new ArrayList<>();
        Address[] rec = message.getRecipients(Message.RecipientType.TO);
        if (rec != null) for (Address a : rec) doc.to.add(a.toString());

        doc.plainText = "";
        doc.htmlText = "";
        doc.tables = new ArrayList<>();
        doc.attachments = new ArrayList<>();

        Object content = message.getContent();
        if (content instanceof String) {
            // plain text
            doc.plainText = ((String) content).trim();
        } else if (content instanceof Multipart) {
            parseMultipart((Multipart) content, doc);
        } else {
            // fallback
            doc.plainText = content == null ? "" : content.toString();
        }


        if ((doc.plainText == null || doc.plainText.isBlank()) && doc.htmlText != null && !doc.htmlText.isBlank()) {
            doc.plainText = htmlCleaner.htmlToText(doc.htmlText);
        }

        if (doc.htmlText != null && !doc.htmlText.isBlank()) {
            doc.htmlText = htmlCleaner.cleanHtml(doc.htmlText);
            doc.tables = tableExtractor.extractTablesFromHtml(doc.htmlText);
        }

        return doc;
    }

    private void parseMultipart(Multipart multi, EmailDocument doc) throws Exception {
        Path temp = Files.createDirectories(Path.of(tempDir));
        for (int i = 0; i < multi.getCount(); i++) {
            BodyPart bp = multi.getBodyPart(i);

            String disp = bp.getDisposition();
            String filename = bp.getFileName();
            if (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT) || filename != null) {
                try (InputStream is = bp.getInputStream()) {
                    AttachmentData ad = attachmentExtractor.extract(filename, is, bp.getSize(), temp);
                    doc.attachments.add(ad);
                }
                continue;
            }

            if (bp.isMimeType("text/plain")) {
                Object o = bp.getContent();
                if (o != null) doc.plainText += o.toString() + "\n";
            } else if (bp.isMimeType("text/html")) {
                Object o = bp.getContent();
                if (o != null) doc.htmlText += o.toString() + "\n";
            } else if (bp.getContent() instanceof Multipart nested) {
                parseMultipart(nested, doc);
            } else {
                Object o = bp.getContent();
                if (o instanceof String) doc.plainText += (String) o;
            }
        }
    }
}
