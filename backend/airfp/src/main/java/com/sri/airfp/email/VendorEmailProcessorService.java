package com.sri.airfp.email;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.airfp.llm.OpenRouterClient;
import com.sri.airfp.llm.PromptBuilder;
import com.sri.airfp.model.EmailDocument;
import com.sri.airfp.model.VendorResponse;
import jakarta.mail.Message;
import org.springframework.stereotype.Service;

@Service
public class VendorEmailProcessorService {

    private final EmailNormalizationService normalizationService;
    private final UnifiedTextBuilder unifiedTextBuilder;
    private final OpenRouterClient llmClient; // your existing client that returns assistant content
    private final ObjectMapper mapper = new ObjectMapper();

    public VendorEmailProcessorService(EmailNormalizationService normalizationService,
                                       UnifiedTextBuilder unifiedTextBuilder,
                                       OpenRouterClient llmClient) {
        this.normalizationService = normalizationService;
        this.unifiedTextBuilder = unifiedTextBuilder;
        this.llmClient = llmClient;
    }

    public VendorResponse processMessage(Message message) throws Exception {
        EmailDocument doc = normalizationService.normalizeMessage(message);
        String unified = unifiedTextBuilder.build(doc);


        String prompt = PromptBuilder.buildVendorPrompt(unified);

        // call LLM ()
        String llmResult = llmClient.callLlm(prompt);


        VendorResponse vr = mapper.readValue(llmResult, VendorResponse.class);


        return vr;
    }
}
