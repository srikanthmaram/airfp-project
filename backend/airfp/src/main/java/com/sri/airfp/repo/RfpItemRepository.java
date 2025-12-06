package com.sri.airfp.repo;


import com.sri.airfp.entity.RfpItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfpItemRepository extends JpaRepository<RfpItemEntity, Long> {}