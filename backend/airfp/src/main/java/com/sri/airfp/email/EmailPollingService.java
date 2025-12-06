package com.sri.airfp.email;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.sri.airfp.entity.VendorResponseEntity;
import com.sri.airfp.entity.VendorSentRecord;
import com.sri.airfp.llm.OpenRouterClient;

import com.sri.airfp.llm.PromptBuilder;
import com.sri.airfp.model.EmailDocument;
import com.sri.airfp.repo.VendorResponseRepository;
import com.sri.airfp.repo.VendorSentRecordRepository;
import com.sri.airfp.util.JsonUtils;
import jakarta.mail.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class EmailPollingService {

    private final EmailReceiverService emailReceiver;
    private final EmailNormalizationService normalizationService;
    private final UnifiedTextBuilder unifiedTextBuilder;
    private final TrackingIdExtractor trackingIdExtractor;
    private final VendorSentRecordRepository sentRepo;
    private final VendorResponseRepository responseRepo;
    private final OpenRouterClient llmClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public EmailPollingService(EmailReceiverService emailReceiver,
                               EmailNormalizationService normalizationService,
                               UnifiedTextBuilder unifiedTextBuilder,
                               TrackingIdExtractor trackingIdExtractor,
                               VendorSentRecordRepository sentRepo,
                               VendorResponseRepository responseRepo,
                               OpenRouterClient llmClient) {
        this.emailReceiver = emailReceiver;
        this.normalizationService = normalizationService;
        this.unifiedTextBuilder = unifiedTextBuilder;
        this.trackingIdExtractor = trackingIdExtractor;
        this.sentRepo = sentRepo;
        this.responseRepo = responseRepo;
        this.llmClient = llmClient;
    }

    // run every 3 minutes
    @Scheduled(fixedDelay = 30000)
    public void pollAndProcess() {

        System.out.println("Heloo , emails are being read.....:");
        List<Message> messages;
        try {
            messages = emailReceiver.fetchUnreadEmails();
        } catch (Exception e) {
            // log & return
            e.printStackTrace();
            return;
        }

        for (Message msg : messages) {
            try {
                handleMessage(msg);
                // mark message seen is done in emailReceiver.fetchUnreadEmails
            } catch (Exception ex) {
                ex.printStackTrace();
                // continue processing other messages
            }
        }
    }

    @Transactional
    protected void handleMessage(Message message) throws Exception {
        EmailDocument doc = normalizationService.normalizeMessage(message);
        String unified = unifiedTextBuilder.build(doc);

        // 1) get tracking id
        String trackingId = trackingIdExtractor.extract(unified);
        if (trackingId == null) {

            return;
        }

        Optional<VendorSentRecord> sentOpt = sentRepo.findByTrackingId(trackingId);
        if (sentOpt.isEmpty()) {

            return;
        }

        VendorSentRecord sentRecord = sentOpt.get();
        if ("RESPONDED".equals(sentRecord.getStatus())) {

            return;
        }

        // 2) Prepare prompt and call LLM
        String prompt = PromptBuilder.buildVendorPrompt(unified);
        String llmJson = llmClient.callLlm(prompt);

        // 3) Parse LLM JSON -> your VendorResponse

        String cleaned = JsonUtils.cleanAndEnsureJsonObjectOrArray(llmJson);
        // store raw
        VendorResponseEntity responseEntity = new VendorResponseEntity();
        responseEntity.setReceivedDate(new Date());
        responseEntity.setVendorEmail(doc.from == null ? sentRecord.getVendorEmail() : doc.from);
        responseEntity.setRfp(sentRecord.getRfp());
        responseEntity.setSentRecord(sentRecord);
        responseEntity.setRawLlmJson(cleaned);

        // 4) map to canonical normalizedJson (pick first element if array)
        String normalized;
        if (cleaned.trim().startsWith("[")) {
            // pick first element
            Object[] arr = mapper.readValue(cleaned, Object[].class);
            normalized = mapper.writeValueAsString(arr[0]);
        } else {
            normalized = cleaned;
        }
        responseEntity.setNormalizedJson(normalized);

        // save
        responseRepo.save(responseEntity);

        // update sent record
        sentRecord.setResponse(responseEntity);
        sentRecord.setStatus("RESPONDED");
        sentRepo.save(sentRecord);
    }
}
