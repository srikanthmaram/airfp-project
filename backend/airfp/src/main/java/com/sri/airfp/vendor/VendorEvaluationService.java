package com.sri.airfp.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.airfp.entity.VendorResponseEntity;
import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.llm.OpenRouterClient;
import com.sri.airfp.model.RecommendationResult;
import com.sri.airfp.repo.RfpRepository;
import com.sri.airfp.repo.VendorResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;

import java.util.List;
@Service
@RequiredArgsConstructor
public class VendorEvaluationService {

    private final OpenRouterClient openRouterClient;
    private final ObjectMapper mapper = new ObjectMapper();


    private final VendorResponseRepository vendorResponseRepository;
    private final RfpRepository rfpRepository;




    public RecommendationResult evaluateUsingAI(Long rfpId) {

        RfpEntity rfp = rfpRepository.findById(rfpId)
                .orElseThrow(() -> new RuntimeException("RFP not found: " + rfpId));

        List<VendorResponseEntity> responses =
                vendorResponseRepository.findByRfpId(rfpId);

        String prompt = buildEvaluationPrompt(rfp, responses);

        String llmOutput = null;
        try {
            llmOutput = openRouterClient.callLlm(prompt);
            String cleaned = cleanJson(llmOutput);

            RecommendationResult result = mapper.readValue(cleaned, RecommendationResult.class);



            return result;

        } catch (Exception e) {
            throw new RuntimeException("LLM returned invalid JSON", e);
        }
    }

    public String buildEvaluationPrompt(RfpEntity rfp, List<VendorResponseEntity> responses) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
        You are an expert procurement analyst.

        Task:
        Evaluate the below vendor responses for the given RFP and provide:
        - A score (0–10) for each vendor
        - A detailed justification
        - A comparison summary
        - Ranking from best to worst
        - Final recommended vendor with reason

        Output EXACT JSON structure:

        {
          "summary": "",
          "best_vendor_id": null,
          "rankings": [
            {
              "vendor_response_id": 0,
              "vendor_name": "",
              "score": 0,
              "strengths": [],
              "weaknesses": [],
              "justification": ""
            }
          ]
        }

        DO NOT return markdown. Only return JSON.
        """);

        // RFP details
        sb.append("\nRFP DETAILS:\n");
        sb.append(rfp.getTitle()).append("\n");
        sb.append(rfp.getDescription()).append("\n");
        sb.append("Budget: ").append(rfp.getBudget()).append("\n\n");

        // Vendor Responses
        sb.append("\nVENDOR RESPONSES:\n");

        for (VendorResponseEntity v : responses) {
            sb.append("VendorResponseID: ").append(v.getId()).append("\n");
            sb.append(v.getNormalizedJson()).append("\n\n");
        }

        return sb.toString();
    }
    private String cleanJson(String s) {
        if (s == null) return "";

        // remove markdown fences
        s = s.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        // remove unwanted characters or whitespace before JSON starts
        int firstBrace = s.indexOf('{');
        if (firstBrace > 0) {
            s = s.substring(firstBrace);
        }

        return s;
    }


}
