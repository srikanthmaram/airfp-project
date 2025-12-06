package com.sri.airfp.email;


import java.util.UUID;

public class TrackingIdGenerator {


    public static String generate(Long rfpId, Long vendorId) {
        String shortId = UUID.randomUUID().toString().substring(0, 8);
        return "RFP-" + rfpId + "-VEN-" + (vendorId == null ? "0" : vendorId) + "-" + shortId;
    }
}

