package com.sri.airfp.repo;

import com.sri.airfp.entity.RfpEntity;
import com.sri.airfp.entity.VendorResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VendorResponseRepository extends JpaRepository<VendorResponseEntity,Long> {
    List<VendorResponseEntity> findByRfpId(Long rfpId);

    @Query("SELECT  v.rfp FROM VendorResponseEntity v")
    List<RfpEntity> findRecentRespondedRfps(Pageable pageable);




}
