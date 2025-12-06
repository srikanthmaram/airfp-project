package com.sri.airfp.util;




public class JsonUtils {


    public static String cleanAndEnsureJsonObjectOrArray(String raw) {
        if (raw == null) return "{}";
        String s = raw.trim();


        if (s.startsWith("```") && s.endsWith("```")) {
            s = s.substring(3, s.length() - 3).trim();
        }


        int idxObj = s.indexOf('{');
        int idxArr = s.indexOf('[');
        int start = -1;
        if (idxObj >= 0 && (idxArr < 0 || idxObj < idxArr)) start = idxObj;
        else start = idxArr;
        if (start > 0) s = s.substring(start).trim();


        s = s.replaceAll(",\\s*}", "}");
        s = s.replaceAll(",\\s*]", "]");


        if (!s.startsWith("{") && !s.startsWith("[")) {

            int st = Math.min(
                    s.indexOf('{') > 0 ? s.indexOf('{') : Integer.MAX_VALUE,
                    s.indexOf('[') > 0 ? s.indexOf('[') : Integer.MAX_VALUE
            );
            if (st != Integer.MAX_VALUE) s = s.substring(st);
            else s = "{}";
        }

        return s;
    }
}

