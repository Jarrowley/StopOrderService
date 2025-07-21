package com.stopworkorder.repository;

import com.stopworkorder.model.CompanyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRecordRepository extends JpaRepository<CompanyRecord, Long> {
    Optional<CompanyRecord> findByCompanyNameAndCompanyNumber(String companyName, String companyNumber);
} 