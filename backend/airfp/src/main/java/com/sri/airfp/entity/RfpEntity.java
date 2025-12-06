package com.sri.airfp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "rfp")
@Data
public class RfpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private Integer budget;
    private Integer deliveryTimelineDays;
    private String warranty;
    private String paymentTerms;

    @OneToMany(mappedBy = "rfp", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude                    // stops infinite recursion in toString()
    private List<RfpItemEntity> items;
}
