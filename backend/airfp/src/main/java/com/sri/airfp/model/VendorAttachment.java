package com.sri.airfp.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class VendorAttachment {

    private String filename;
    private String notes;

    private Map<String, Object> extra_attachment_fields = new HashMap<>();

    @JsonAnySetter
    public void addExtra(String key, Object value) {
        extra_attachment_fields.put(key, value);
    }
}
