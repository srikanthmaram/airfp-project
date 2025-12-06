package com.sri.airfp.llm;


public class PromptBuilder {

    public static String buildVendorPrompt(String unifiedText) {
        return """
        You are a procurement extraction assistant.
Convert the vendor's full response into EXACT JSON following this schema:

{
  "vendor_name": "",
  "vendor_email": "",
  "items": [
    {
      "item_name": "",
      "quantity": 0,
      "unit_price": null,
      "total_price": null,
      "delivery_days": null,
      "specs": {},
      "extra_item_fields": {}
    }
  ],
  "total_price": null,
  "currency": "",
  "warranty": "",
  "delivery_timeline_days": null,
  "attachments": [
    {
      "filename": "",
      "notes": "",
      "extra_attachment_fields": {}
    }
  ],
  "notes": "",
  "extra_fields": {}
}

STRICT RULES:
- Output ONLY valid JSON. No explanations, no markdown, no comments, no backticks.
- No text before or after the JSON.
- All numbers must be numeric (no quotes around prices or quantities).
- If a field is missing, use null or empty string.
- Specs must be an object: { "key": "value" }.
- Convert currency symbols into standard codes: ₹ → "INR", $ → "USD".
- Store any extra unexpected fields inside:
  - extra_item_fields
  - extra_attachment_fields
  - extra_fields

Vendor message contents (email body + HTML + extracted attachment text + table text) are below:
""" + unifiedText;
    }
    public static String buildRfpJSONPrompt(String rfpText){
        return """
                    You are an expert procurement analyst.
                    Extract all structured RFP details from the following text:
    
                    RFP Input:
                    """ + rfpText+ """
    
                    Follow this EXACT JSON schema:
    
                    {
                      "title": "",
                      "description": "",
                      "items": [
                        {
                          "item_name": "",
                          "quantity": 0,
                          "specs": {},
                          "additional_fields": {}
                        }
                      ],
                      "budget": null,
                      "delivery_timeline_days": null,
                      "warranty": "",
                      "payment_terms": "",
                      "additional_requirements": []
                    }
    
                    Rules:
                    - If info missing → use null or empty string.
                    - Output ONLY raw JSON.
                    - Do NOT use backticks.
                    - Do NOT use markdown.
                    - Do NOT add explanations.
                    """;

    }
}

