package com.sri.airfp.model;


import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class AttachmentData {
    private String filename;
    private Long size;
    private String mimeType;
    private String localPath;
    private String extractedText;

    private Map<String, Object> extraFields = new HashMap<>();

    @JsonAnySetter
    public void setExtra(String key, Object value) {
        extraFields.put(key, value);
    }
}


