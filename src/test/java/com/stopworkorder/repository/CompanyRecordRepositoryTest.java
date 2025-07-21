package com.stopworkorder.repository;

import com.stopworkorder.model.CompanyRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRecordRepositoryTest {
    @Autowired
    private CompanyRecordRepository repository;

    @Test
    void testSaveAndFindByCompanyNameAndCompanyNumber() {
        CompanyRecord record = new CompanyRecord();
        record.setCompanyName("Test Company");
        record.setCompanyNumber("123456");
        record.setDateAdded(LocalDate.now());
        repository.save(record);

        Optional<CompanyRecord> found = repository.findByCompanyNameAndCompanyNumber("Test Company", "123456");
        assertTrue(found.isPresent());
        assertEquals("Test Company", found.get().getCompanyName());
        assertEquals("123456", found.get().getCompanyNumber());
    }
} 