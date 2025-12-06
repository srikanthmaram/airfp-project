package com.sri.airfp.model;


import java.util.Date;
import java.util.List;

public class EmailDocument {
    public String messageId;
    public String subject;
    public String from;
    public List<String> to;
    public Date receivedDate;

    public String plainText;
    public String htmlText;
    public List<Table> tables;
    public List<AttachmentData> attachments;
}
