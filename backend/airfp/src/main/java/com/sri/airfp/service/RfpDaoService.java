package com.sri.airfp.service;

import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.entity.RfpItemEntity;
import com.sri.airfp.model.RfpResponse;
import com.sri.airfp.repo.RfpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RfpDaoService {
    private final RfpRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public RfpEntity saveRfp(RfpResponse response) throws Exception {




        RfpEntity rfp = new RfpEntity();
        rfp.setTitle(response.title);
        rfp.setDescription(response.description);
        rfp.setBudget(response.budget);
        rfp.setDeliveryTimelineDays(response.delivery_timeline_days);
        rfp.setWarranty(response.warranty);
        rfp.setPaymentTerms(response.payment_terms);


        List<RfpItemEntity> entities = response.items.stream().map(item -> {
            try {
                RfpItemEntity e = new RfpItemEntity();

                e.setItemName(item.item_name);
                e.setQuantity(item.quantity);
                e.setSpecsJson(mapper.writeValueAsString(item.specs));
                e.setAdditionalFieldsJson(mapper.writeValueAsString(item.additional_fields));
                e.setRfp(rfp);
                return e;
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());

        rfp.setItems(entities);


        return repository.save(rfp);
    }

    public RfpEntity getRfpDetails(Long id) {
        return repository.getReferenceById(id);
    }

    public List<RfpEntity> getAllRfps() {
        return repository.findAll();
    }
}
