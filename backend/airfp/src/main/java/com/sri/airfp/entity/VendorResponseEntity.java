package com.sri.airfp.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "vendor_response")
@Data
public class VendorResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfp_id")
    @ToString.Exclude
    @JsonBackReference
    private RfpEntity rfp;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_record_id")
    @JsonBackReference
    @ToString.Exclude
    private VendorSentRecord sentRecord;

    private String vendorEmail;

    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String rawLlmJson;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String normalizedJson;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String notes;
}

