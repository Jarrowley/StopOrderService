package com.stopworkorder.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CompanyRecordTest {
    @Test
    void testGettersAndSetters() {
        CompanyRecord record = new CompanyRecord();
        record.setId(1L);
        record.setCompanyName("Test Company");
        record.setCompanyNumber("123456");
        LocalDate date = LocalDate.of(2024, 6, 1);
        record.setDateAdded(date);

        assertEquals(1L, record.getId());
        assertEquals("Test Company", record.getCompanyName());
        assertEquals("123456", record.getCompanyNumber());
        assertEquals(date, record.getDateAdded());
    }
} 