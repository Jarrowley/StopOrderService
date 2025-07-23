package com.stopworkorder.service;

import com.stopworkorder.model.CompanyRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final String from;
    private final String emailTemplate;

    public EmailService(JavaMailSender mailSender, @Value("${mail.username}") String from, @Value("${notification.email-template}") String emailTemplate) {
        this.mailSender = mailSender;
        this.from = from;
        this.emailTemplate = emailTemplate;
    }

    public void sendNewCompanyNotification(List<String> recipients, CompanyRecord company) {
        String subject = "New Company in Liquidation: " + company.getCompanyName();
        String body = emailTemplate
            .replace("{{companyName}}", company.getCompanyName())
            .replace("{{companyNumber}}", company.getCompanyNumber())
            .replace("{{dateAdded}}", company.getDateAdded().format(DateTimeFormatter.ISO_DATE));
        for (String recipient : recipients) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(recipient);
            message.setSubject(subject);
            message.setText(body);
            log.info("body: {}", body);
            mailSender.send(message);
            log.info("Sent email to {}: {}", recipient, subject);
        }
    }
} 