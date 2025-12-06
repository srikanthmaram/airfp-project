package com.sri.airfp.email;


import com.sri.airfp.entity.RfpEntity;

import com.sri.airfp.entity.VendorSentRecord;
import com.sri.airfp.repo.VendorSentRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VendorMessagingService {

    private final VendorSentRecordRepository sentRepo;
    private final EmailService emailService;
    private final VendorEmailTemplate template;

    public VendorMessagingService(VendorSentRecordRepository sentRepo,
                                  EmailService emailService,
                                  VendorEmailTemplate template) {
        this.sentRepo = sentRepo;
        this.emailService = emailService;
        this.template = template;
    }


    @Transactional
    public void sendRfpToVendors(RfpEntity rfp, List<VendorInfo> vendors) {

        for (VendorInfo v : vendors) {
            String trackingId = TrackingIdGenerator.generate(rfp.getId(), v.getId());
            VendorSentRecord rec = new VendorSentRecord();
            rec.setRfp(rfp);
            rec.setVendorEmail(v.getEmail());

            rec.setTrackingId(trackingId);
            rec.setSentTimestamp(new Date());
            rec.setStatus("SENT");
            System.out.println("helooo"+rec.toString());
            sentRepo.save(rec);

            // build email body
            String body = template.buildPlainBody(rfp, trackingId);
            String subject = "RFP Request: " + rfp.getTitle();

            try {
                emailService.sendSimpleMail(v.getEmail(), subject, body);
            } catch (Exception ex) {
                rec.setStatus("FAILED");
                sentRepo.save(rec);
            }
        }
    }


    public static class VendorInfo {
        private Long id;
        private String email;
        public VendorInfo(Long id, String email) { this.id = id; this.email = email; }
        public Long getId(){ return id; } public String getEmail(){ return email; }
    }
}

