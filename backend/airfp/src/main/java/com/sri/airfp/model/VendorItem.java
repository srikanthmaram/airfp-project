package com.sri.airfp.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class VendorItem {

    private String item_name;
    private Integer quantity;
    private Double unit_price;
    private Double total_price;
    private Integer delivery_days;

    private Map<String, Object> specs;

    private Map<String, Object> extra_item_fields = new HashMap<>();

    @JsonAnySetter
    public void addExtra(String key, Object value) {
        extra_item_fields.put(key, value);
    }
}
