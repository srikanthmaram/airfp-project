package com.sri.airfp.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class HtmlCleaner {


    public String cleanHtml(String html) {
        if (html == null) return "";
        Document doc = Jsoup.parse(html);
        doc.select("script, style, meta, link, iframe, noscript").remove();
        // remove tiny images often used for tracking
        for (Element img : doc.select("img")) {
            String w = img.attr("width");
            String h = img.attr("height");
            if ((w != null && (w.equals("1") || w.equals("0"))) || (h != null && (h.equals("1")||h.equals("0")))) {
                img.remove();
            } else {

                String alt = img.attr("alt");
                if (alt != null && !alt.isBlank()) img.after(alt);
                img.remove();
            }
        }
        // normalize whitespace
        String cleaned = doc.body() == null ? "" : doc.body().html();
        return cleaned.replaceAll("\\s+", " ").trim();
    }

    public String htmlToText(String html) {
        if (html == null) return "";
        Document doc = Jsoup.parse(html);
        return doc.text().replaceAll("\\s+", " ").trim();
    }
}
