package com.stopworkorder.service;

import com.stopworkorder.property.SchedulerProperties;
import com.stopworkorder.model.CompanyRecord;
import com.stopworkorder.repository.CompanyRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyRecordService {
    private final CompanyRecordRepository repository;
    private final EmailService emailService;
    private final SchedulerProperties schedulerProperties;

    @Transactional
    public void processNewCompanies(List<CompanyRecord> scrapedCompanies) {
        for (CompanyRecord company : scrapedCompanies) {
            boolean exists = repository.findByCompanyNameAndCompanyNumber(company.getCompanyName(), company.getCompanyNumber()).isPresent();
            if (!exists) {
                log.info("New company detected, saving to database and sending email: {}", company.getCompanyName());
                repository.save(company);
                emailService.sendNewCompanyNotification(schedulerProperties.getRecipients(), company);
            }
        }
    }
} 