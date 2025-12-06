package com.sri.airfp.repo;


import com.sri.airfp.entity.RfpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfpRepository extends JpaRepository<RfpEntity, Long> {}