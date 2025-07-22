package com.stopworkorder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScraperServiceTest {
    private ScraperService scraperService;

    @BeforeEach
    void setUp() {
        scraperService = new ScraperService();
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