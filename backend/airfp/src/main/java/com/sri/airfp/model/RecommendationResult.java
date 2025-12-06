package com.sri.airfp.model;


import lombok.Data;

import java.util.List;

@Data

public class RecommendationResult {



    private String summary;

    private Long best_vendor_id;


    private List<VendorRanking> rankings;
}
