package com.sri.airfp.repo;



import com.sri.airfp.entity.VendorSentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VendorSentRecordRepository extends JpaRepository<VendorSentRecord, Long> {
    Optional<VendorSentRecord> findByTrackingId(String trackingId);
    @Query("SELECT COUNT(DISTINCT v.rfp.id) FROM VendorSentRecord v")
    long countUniqueRfpSent();
}

