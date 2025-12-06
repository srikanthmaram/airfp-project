package com.sri.airfp.rfp;


import com.sri.airfp.email.EmailService;
import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.entity.RfpItemEntity;
import com.sri.airfp.entity.VendorEntity;
import com.sri.airfp.repo.RfpRepository;
import com.sri.airfp.repo.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RfpProcessingService {

    private final EmailService emailService;
    private final VendorRepository vendorRepo;
    private final RfpRepository rfpRepo;

    public void sendRfpToVendors(Long rfpId, List<Long> vendorIds) {
        RfpEntity rfp = rfpRepo.findById(rfpId)
                .orElseThrow(() -> new RuntimeException("RFP not found"));

        List<VendorEntity> vendors = vendorRepo.findAllById(vendorIds);

        for (VendorEntity v : vendors) {

            String subject = "RFP Request: " + rfp.getTitle();
            String body = buildVendorRfpEmail(rfp);

            emailService.sendSimpleMail(v.getEmail(), subject, body);
        }
    }

    private String buildVendorRfpEmail(RfpEntity rfp) {
        return """
               Dear Vendor,

               Please find the RFP details below:

               Title: %s
               Description: %s
               Budget: %s
               Delivery: %s days

               Items:
               %s

               Regards,
               Procurement Team
               """.formatted(
                rfp.getTitle(),
                rfp.getDescription(),
                rfp.getBudget(),
                rfp.getDeliveryTimelineDays(),
                formatRfpItems(rfp.getItems())
        );
    }
    public String formatRfpItems(List<RfpItemEntity> items) {
        StringBuilder sb = new StringBuilder();

        for (RfpItemEntity item : items) {
            sb.append("- Item: ").append(item.getItemName()).append("\n");
            sb.append("  Quantity: ").append(item.getQuantity()).append("\n");

            if (item.getSpecsJson() != null) {
                sb.append("  Specs: ").append(item.getSpecsJson()).append("\n");
            }

            if (item.getAdditionalFieldsJson() != null) {
                sb.append("  Additional: ").append(item.getAdditionalFieldsJson()).append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

}
