package com.sri.airfp.service;

import com.sri.airfp.entity.VendorEntity;
import com.sri.airfp.entity.VendorResponseEntity;
import com.sri.airfp.model.VendorRequest;
import com.sri.airfp.model.VendorResponse;
import com.sri.airfp.repo.VendorRepository;
import com.sri.airfp.repo.VendorResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorDaoService {
    private final VendorRepository vendorRepository;
    private final VendorResponseRepository vendorResponseRepository;

    public VendorEntity saveVendor(VendorRequest vendorRequestResponse) {
        VendorEntity vendorEntity = new VendorEntity();
        vendorEntity.setCategory(vendorRequestResponse.category);
        vendorEntity.setName(vendorRequestResponse.name);
        vendorEntity.setPhone(vendorRequestResponse.phone);
        vendorEntity.setEmail(vendorRequestResponse.email);


        return vendorRepository.save(vendorEntity);

    }

    public List<VendorEntity> getVendors() {
        return vendorRepository.findAll();
    }

    public String getVendorEmail(Long id) {

        Optional<VendorEntity> vendorEntityOptional = vendorRepository.findById(id);

        if (vendorEntityOptional.isPresent()) {
            VendorEntity vendorEntity = vendorEntityOptional.get();

            return vendorEntity.getEmail();
        } else {

            return null;
        }
    }

    public List<VendorResponseEntity> getVendorResponses(Long rfpId){
        return vendorResponseRepository.findByRfpId(rfpId);
    }
}
