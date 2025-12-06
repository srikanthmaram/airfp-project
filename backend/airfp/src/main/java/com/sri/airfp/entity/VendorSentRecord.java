package com.sri.airfp.entity;

import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.entity.VendorResponseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "vendor_sent_record")
@Data
public class VendorSentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RfpEntity rfp;

    private String vendorEmail;
    private String trackingId;

    private Date sentTimestamp;

    @OneToOne
    private VendorResponseEntity response;

    private String status;
}
