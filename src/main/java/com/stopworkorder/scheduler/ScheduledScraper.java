package com.stopworkorder.scheduler;

import com.stopworkorder.property.SchedulerProperties;
import com.stopworkorder.service.CompanyRecordService;
import com.stopworkorder.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ScheduledScraper {
    private final ScraperService scraperService;
    private final CompanyRecordService companyRecordService;
    private final SchedulerProperties schedulerProperties;

    @Scheduled(cron = "#{@schedulerProperties.cron}")
    public void runScraper() {
        companyRecordService.processNewCompanies(scraperService.scrape());
    }
} 