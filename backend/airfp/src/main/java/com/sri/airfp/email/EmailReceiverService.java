package com.sri.airfp.email;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@Service
public class EmailReceiverService {

    @Value("${mail.imap.host}")
    private String host;

    @Value("${mail.imap.username}")
    private String username;

    @Value("${mail.imap.password}")
    private String password;

    public List<Message> fetchUnreadEmails() throws Exception {



        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.starttls.enable", "true");

        props.put("mail.imap.host", host);
        props.put("mail.imap.port", "993");

        props.put("mail.imap.connectiontimeout", "5000");
        props.put("mail.imap.timeout", "5000");
        props.put("mail.imap.writetimeout", "5000");

        Session session = Session.getInstance(props);
        session.setDebug(true); // <-- important log

        Store store = session.getStore("imap");
        store.connect(host, 993, username, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        Message[] messages = inbox.search(
                new FlagTerm(new Flags(Flags.Flag.SEEN), false)
        );

       List<Message> emailMessage = new ArrayList<>();
        int i=0;

        for (Message message : messages) {
            MimeMessage copied = new MimeMessage((MimeMessage) message);
            emailMessage.add(copied);
            if(i==5)
                break;

            i++;

            message.setFlag(Flags.Flag.SEEN, true);
        }

        inbox.close(false);
        store.close();

        return emailMessage;
    }

    private String extractContent(Message message) throws Exception {
        Object content = message.getContent();

        if (content instanceof String) {
            return (String) content;
        }
        else if (content instanceof Multipart multipart) {
            return getTextFromMultipart(multipart);
        }

        return "";
    }

    private String getTextFromMultipart(Multipart multipart) throws Exception {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);

            if (part.isMimeType("text/plain")) {
                result.append(part.getContent());
            }
            else if (part.isMimeType("text/html")) {
                result.append(Jsoup.parse(part.getContent().toString()).text());
            }
        }

        return result.toString();
    }
}
