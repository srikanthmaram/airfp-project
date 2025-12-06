package com.sri.airfp.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VendorSelectionRequest {
    private List<Long> vendorIds;

    public List<Long> getVendorIds() {
        return vendorIds;
    }

    public void setVendorIds(List<Long> vendorIds) {
        this.vendorIds = vendorIds;
    }
}

