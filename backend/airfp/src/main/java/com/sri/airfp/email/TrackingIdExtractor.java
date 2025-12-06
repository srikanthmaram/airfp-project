package com.sri.airfp.email;


import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TrackingIdExtractor {

    // tolerate a few variants and whitespace
    private static final Pattern PATTERN = Pattern.compile("RFP-TRACKING-ID:\\s*([A-Za-z0-9\\-]+)", Pattern.CASE_INSENSITIVE);

    public String extract(String text) {
        if (text == null) return null;
        Matcher m = PATTERN.matcher(text);
        return m.find() ? m.group(1) : null;
    }
}

