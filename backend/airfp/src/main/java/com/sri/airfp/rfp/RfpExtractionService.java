package com.sri.airfp.rfp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.airfp.llm.OpenRouterClient;
import com.sri.airfp.llm.PromptBuilder;
import com.sri.airfp.model.RfpExtractRequest;
import com.sri.airfp.model.RfpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public  class RfpExtractionService {

    private final OpenRouterClient openRouterClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public RfpResponse extractRfp(RfpExtractRequest request) throws Exception {


        String prompt = PromptBuilder.buildRfpJSONPrompt(request.getRfpText());

        String jsonString = openRouterClient.callLlm(prompt);



        return mapper.readValue(jsonString, RfpResponse.class);
    }
}
