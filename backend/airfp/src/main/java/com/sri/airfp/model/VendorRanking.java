package com.sri.airfp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data

public class VendorRanking {



    private Long vendor_response_id;
    private String vendor_name;
    private double score;


    private List<String> strengths;


    private List<String> weaknesses;

    private String justification;

}
