package com.sri.airfp.conroller;

import com.sri.airfp.entity.VendorResponseEntity;
import com.sri.airfp.model.VendorSelectionRequest;
import com.sri.airfp.service.VendorDaoService;
import com.sri.airfp.entity.VendorEntity;
import com.sri.airfp.model.VendorRequest;
import com.sri.airfp.vendor.VendorProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin(origins ="http://localhost:5173")
@RequiredArgsConstructor
public class VendorController {
    private final VendorDaoService vendorDaoService;

    private final VendorProposalService vendorProposalService;


    @PostMapping("/add")
    public VendorEntity addVendor(@RequestBody VendorRequest vendorRequestResponse){
        return vendorDaoService.saveVendor(vendorRequestResponse);
    }

    @GetMapping("/vendors")
    public List<VendorEntity> getAllVendors(){
        return vendorDaoService.getVendors();

    }

    @GetMapping("/getvendors")
    public List<VendorEntity> getVendors(){
        return vendorDaoService.getVendors();
    }

    @PostMapping("/{rfpId}/sendToVendors")
    public void sendToVendors(@PathVariable Long rfpId, @RequestBody VendorSelectionRequest vendorIds) {
        vendorProposalService.sendProposal(rfpId,vendorIds);

    }

    @GetMapping("/vendor-responses/rfp/{rfpId}")
    public List<VendorResponseEntity> getVendorResponses(@PathVariable Long rfpId){

      return vendorDaoService.getVendorResponses(rfpId);
    }
}
