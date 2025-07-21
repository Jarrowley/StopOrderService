package com.stopworkorder.service;

import com.stopworkorder.property.SchedulerProperties;
import com.stopworkorder.model.CompanyRecord;
import com.stopworkorder.repository.CompanyRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyRecordServiceTest {
    private CompanyRecordRepository repository;
    private EmailService emailService;
    private SchedulerProperties schedulerProperties;
    private CompanyRecordService service;

    @BeforeEach
    void setUp() {
        repository = mock(CompanyRecordRepository.class);
        emailService = mock(EmailService.class);
        schedulerProperties = mock(SchedulerProperties.class);
        when(schedulerProperties.getRecipients()).thenReturn(Collections.singletonList("test@example.com"));
        service = new CompanyRecordService(repository, emailService, schedulerProperties);
    }

    @Test
    void testProcessNewCompanies_savesAndNotifies() {
        CompanyRecord company = new CompanyRecord();
        company.setCompanyName("Test Company");
        company.setCompanyNumber("123456");
        company.setDateAdded(LocalDate.now());
        when(repository.findByCompanyNameAndCompanyNumber(anyString(), anyString())).thenReturn(Optional.empty());

        service.processNewCompanies(Collections.singletonList(company));

        verify(repository, times(1)).save(company);
        verify(emailService, times(1)).sendNewCompanyNotification(anyList(), eq(company));
    }

    @Test
    void testProcessNewCompanies_doesNotNotifyIfExists() {
        CompanyRecord company = new CompanyRecord();
        company.setCompanyName("Test Company");
        company.setCompanyNumber("123456");
        company.setDateAdded(LocalDate.now());
        when(repository.findByCompanyNameAndCompanyNumber(anyString(), anyString())).thenReturn(Optional.of(company));

        service.processNewCompanies(Collections.singletonList(company));

        verify(repository, never()).save(any());
        verify(emailService, never()).sendNewCompanyNotification(anyList(), any());
    }
} 