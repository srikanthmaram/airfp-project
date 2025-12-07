package com.sri.airfp.vendor;

import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.model.VendorSelectionRequest;
import com.sri.airfp.repo.RfpRepository;
import com.sri.airfp.service.VendorDaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorProposalService {
    private final RfpRepository rfpRepository;
    private final VendorDaoService vendorDaoService;
    private final VendorMessagingService vendorMessagingService;


    public void sendProposal(Long rfpId, VendorSelectionRequest vendorSelectionRequest) {
        RfpEntity rfp = rfpRepository.findById(rfpId).orElseThrow(() -> new RuntimeException("RFP not found"));


        List<Long> vendor = vendorSelectionRequest.getVendorIds();
        List<VendorMessagingService.VendorInfo> vendors = vendor.stream()
                .map(id -> {
                    String email = vendorDaoService.getVendorEmail(id);
                    return new VendorMessagingService.VendorInfo(id, email);
                }).toList();

        vendorMessagingService.sendRfpToVendors(rfp, vendors);
    }
}
