package com.sri.airfp.conroller;

import com.sri.airfp.vendor.VendorEvaluationService;

import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.model.RecommendationResult;
import com.sri.airfp.model.RfpExtractRequest;
import com.sri.airfp.model.RfpResponse;
import com.sri.airfp.service.RfpDaoService;
import com.sri.airfp.rfp.RfpExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@CrossOrigin(origins ="http://localhost:5173")
@RequestMapping("/api/rfp")
@RequiredArgsConstructor
public class RfpController {

    private final RfpExtractionService extractionService;

    private final RfpDaoService rfpDaoService;
    private static final Logger logger = LoggerFactory.getLogger(RfpController.class);
    private final VendorEvaluationService vendorEvaluationService;





    @PostMapping("/extract")
    public RfpResponse extract(@RequestBody RfpExtractRequest request) throws Exception {

        logger.info("Received POST request to /api/rfp/extract. Request details: {}", request.toString());
        RfpResponse extracted = extractionService.extractRfp(request);
       // rfpDaoService.saveRfp(extracted);

        return extracted;
    }



    @PostMapping("/create")
    public RfpEntity createRfp(@RequestBody RfpResponse request) throws Exception {

        logger.info("Received POST request to /api/rfp/create. Request details: {}", request.toString());

        return  rfpDaoService.saveRfp(request);
    }

    @GetMapping("/{id}")
    public RfpEntity getRfp(@PathVariable Long id){
        return rfpDaoService.getRfpDetails(id);

    }

    @GetMapping("/rfps")
    public List<RfpEntity> getAllRfps(){
        return rfpDaoService.getAllRfps();
    }


    @GetMapping("/{rfpId}/evaluation")
    public RecommendationResult evaluateVendors(@PathVariable Long rfpId) {

        return vendorEvaluationService.evaluateUsingAI(rfpId);
    }





}