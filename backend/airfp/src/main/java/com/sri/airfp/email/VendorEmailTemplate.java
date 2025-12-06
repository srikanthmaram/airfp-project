package com.sri.airfp.email;


import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.entity.RfpItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendorEmailTemplate {

    public String buildPlainBody(RfpEntity rfp, String trackingId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear Vendor,\n\n");
        sb.append("Please find the RFP details below:\n\n");
        sb.append("Title: ").append(nullSafe(rfp.getTitle())).append("\n");
        sb.append("Description: ").append(nullSafe(rfp.getDescription())).append("\n");
        sb.append("Budget: ").append(rfp.getBudget()).append("\n");
        sb.append("Delivery: ").append(rfp.getDeliveryTimelineDays()).append(" days\n\n");
        sb.append("Items:\n");
        List<RfpItemEntity> items = rfp.getItems();
        if (items != null) {
            for (RfpItemEntity it : items) {
                sb.append("- Item: ").append(it.getItemName()).append("\n");
                sb.append("  Quantity: ").append(it.getQuantity()).append("\n");
                if (it.getSpecsJson() != null) sb.append("  Specs: ").append(it.getSpecsJson()).append("\n");
                sb.append("\n");
            }
        }
        sb.append("RFP-TRACKING-ID: ").append(trackingId).append("\n\n");
        sb.append("Regards,\nProcurement Team\n");
        return sb.toString();
    }

    private String nullSafe(String s) { return s == null ? "" : s; }
}

