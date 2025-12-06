package com.sri.airfp.model;

import lombok.Data;
import java.util.List;

@Data
public class DashboardSummaryResponse {

    private Stats stats;
    private List<ActivityItem> recentActivity;
    private List<RecommendationItem> recentRecommendations;

    @Data
    public static class Stats {
        private long totalRfps;
        private long sentRfps;
        private long responsesReceived;
        private long rfpsWithRecommendations;
        private long vendorCount;
    }

    @Data
    public static class ActivityItem {
        private String text;
        private String time;
    }

    @Data
    public static class RecommendationItem {
        private String summary;
        private Long best_vendor_id;
        private List<VendorRanking> rankings;

    }
}
