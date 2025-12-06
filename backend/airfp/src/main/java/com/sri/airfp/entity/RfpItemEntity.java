package com.sri.airfp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "rfp_item")
@Data
public class RfpItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private Integer quantity;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String specsJson;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String additionalFieldsJson;

    @ManyToOne
    @JoinColumn(name = "rfp_id")
    @JsonBackReference
    @ToString.Exclude           // stops infinite recursion in toString()
    private RfpEntity rfp;
}
