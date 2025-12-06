package com.sri.airfp.util;


import com.sri.airfp.model.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TableExtractor {

    public List<Table> extractTablesFromHtml(String html) {
        List<Table> out = new ArrayList<>();
        if (html == null || html.isBlank()) return out;

        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table");
        for (Element t : tables) {
            Table table = new Table();
            table.headers = new ArrayList<>();
            table.rows = new ArrayList<>();

            Elements ths = t.select("th");
            if (!ths.isEmpty()) {
                for (Element th : ths) table.headers.add(th.text().trim());
            }

            Elements rows = t.select("tr");
            for (Element row : rows) {
                // skip header row if th used
                if (!table.headers.isEmpty() && !row.select("td").isEmpty() && row.select("th").size()>0) continue;
                List<String> cells = new ArrayList<>();
                Elements tds = row.select("td, th");
                for (Element td : tds) {
                    String text = td.text().trim().replaceAll("\\s+", " ");
                    cells.add(text);
                }
                if (!cells.isEmpty()) table.rows.add(cells);
            }

            // If no headers found, try first row as headers
            if (table.headers.isEmpty() && !table.rows.isEmpty()) {
                table.headers = new ArrayList<>(table.rows.get(0));
                if (table.rows.size() > 1) table.rows = table.rows.subList(1, table.rows.size());
                else table.rows = new ArrayList<>();
            }

            out.add(table);
        }
        return out;
    }

    public List<java.util.Map<String,String>> toListOfMaps(Table table) {
        List<java.util.Map<String,String>> res = new ArrayList<>();
        for (List<String> row : table.rows) {
            java.util.Map<String,String> m = new java.util.HashMap<>();
            for (int i = 0; i < table.headers.size(); i++) {
                String h = table.headers.get(i);
                String val = i < row.size() ? row.get(i) : "";
                m.put(h, val);
            }
            res.add(m);
        }
        return res;
    }
}
