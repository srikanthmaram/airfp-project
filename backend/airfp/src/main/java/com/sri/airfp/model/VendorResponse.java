package com.sri.airfp.model;


import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.*;
import java.util.Map;
@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class VendorResponse {

    private String vendor_name;
    private String vendor_email;

    private List<VendorItem> items;

    private Double total_price;
    private String currency;
    private String warranty;
    private Integer delivery_timeline_days;

    private List<VendorAttachment> attachments;

    private String notes;


    private Map<String, Object> extra_fields = new HashMap<>();

    @JsonAnySetter
    public void addExtra(String key, Object value) {
        extra_fields.put(key, value);
    }
}

