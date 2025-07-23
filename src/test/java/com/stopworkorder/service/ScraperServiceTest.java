package com.stopworkorder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stopworkorder.property.SchedulerProperties;

@ExtendWith(MockitoExtension.class)
class ScraperServiceTest {
    private ScraperService scraperService;

    @BeforeEach
    void setUp() {
        SchedulerProperties schedulerProperties = new SchedulerProperties();
        schedulerProperties.setWebUrl("https://www.nsw.gov.au/departments-and-agencies/building-commission/register-of-building-work-orders?page=2");
        scraperService = new ScraperService(schedulerProperties);
    }

    @Test
    void testLookupAbnByCompanyNameHayat() {
        String abn = scraperService.lookupAbnByCompanyName("Hayat Constructions Pty Ltd");
        assertEquals("64671128804", abn);
    }

    @Test
    void testLookupAbnByCompanyNameStructuralMaster() {
        String abn = scraperService.lookupAbnByCompanyName("Structural Master Construction Pty Ltd");
        assertEquals("93668539008", abn);
    }
} 