package com.sri.airfp.llm;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OpenRouterClient {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.url}")
    private String apiUrl;



    private final ObjectMapper mapper = new ObjectMapper();

    public String callLlm(String prompt) throws Exception {

        WebClient client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("HTTP-Referer", "http://localhost")
                .defaultHeader("X-Title", "RFP-Extractor")
                .build();

        // Build request body
        String requestBody = mapper.writeValueAsString(
                mapper.createObjectNode()
                        .put("model", "meta-llama/llama-3.3-70b-instruct")
                        .set("messages",
                                mapper.createArrayNode()
                                        .add(mapper.createObjectNode()
                                                .put("role", "user")
                                                .put("content", prompt)
                                        )
                        )
        );

        // Call LLM
        String response = client.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Parse raw JSON
        JsonNode root = mapper.readTree(response);

        // Extract ONLY the assistant content
        String content = root
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText();

        return content;
    }
}
