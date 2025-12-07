package com.sri.airfp.model;

import lombok.Data;

import java.util.List;

@Data
public class RfpResponse {
    public String title;
    public String description;
    public List<RfpItem> items;
    public Integer budget;
    public Integer delivery_timeline_days;
    public String warranty;
    public String payment_terms;
    public List<String> additional_requirements;
}