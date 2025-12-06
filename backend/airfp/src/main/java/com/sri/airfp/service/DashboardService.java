package com.sri.airfp.service;



import com.sri.airfp.model.DashboardSummaryResponse;
import com.sri.airfp.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RfpRepository rfpRepo;
    private final VendorSentRecordRepository sentRepo;
    private final VendorResponseRepository responseRepo;
    private final VendorRepository vendorRepo;


    public DashboardSummaryResponse getSummary() {

        DashboardSummaryResponse dto = new DashboardSummaryResponse();

        // ---------- 1. STATS ----------
        DashboardSummaryResponse.Stats stats = new DashboardSummaryResponse.Stats();
        stats.setTotalRfps(rfpRepo.count());
        stats.setSentRfps(sentRepo.countUniqueRfpSent());
        stats.setResponsesReceived(responseRepo.count());
        stats.setVendorCount(vendorRepo.count());

        long rfpsWithRec = responseRepo.findRecentRespondedRfps(PageRequest.of(0, 100))
                .stream()
                .filter(rfp -> responseRepo.findByRfpId(rfp.getId()).size() > 1)
                .count();

        stats.setRfpsWithRecommendations(rfpsWithRec);

        dto.setStats(stats);


        // ---------- 2. RECENT ACTIVITY ----------
        List<DashboardSummaryResponse.ActivityItem> activity = new ArrayList<>();

        sentRepo.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id")))
                .forEach(s -> {
                    DashboardSummaryResponse.ActivityItem item = new DashboardSummaryResponse.ActivityItem();
                    item.setText("RFP sent to vendor: " + s.getVendorEmail());
                    item.setTime(s.getSentTimestamp().toString());
                    activity.add(item);
                });

        responseRepo.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id")))
                .forEach(r -> {
                    DashboardSummaryResponse.ActivityItem item = new DashboardSummaryResponse.ActivityItem();
                    item.setText("Vendor response received from " + r.getVendorEmail());
                    item.setTime(r.getReceivedDate().toString());
                    activity.add(item);
                });

        dto.setRecentActivity(activity);





        return dto;
    }
}
