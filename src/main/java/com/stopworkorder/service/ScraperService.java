package com.stopworkorder.service;

import com.stopworkorder.model.CompanyRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ScraperService {
    private static final String TARGET_URL = "https://www.nsw.gov.au/departments-and-agencies/building-commission/register-of-building-work-orders";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy");

    public List<CompanyRecord> scrape() {
        List<CompanyRecord> companies = new ArrayList<>();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get(TARGET_URL);
            List<WebElement> items = driver.findElements(By.className("nsw-list-item__content"));
            for (WebElement item : items) {
                // Example parsing logic, adjust selectors as needed
                String companyName = item.findElement(By.tagName("h3")).getText();
                Matcher matcher = Pattern.compile("^Stop Work Order for (.+)$").matcher(companyName);
                if (matcher.find()) {
                    companyName = matcher.group(1);
                } else {
                    continue;
                }
                
                String companyNumber = lookupAbnByCompanyName(companyName);
                String dateStr = item.findElement(By.className("nsw-list-item__info")).getText();
                LocalDate dateAdded = LocalDate.parse(dateStr, DATE_FORMATTER); // Parse with custom format
                CompanyRecord company = new CompanyRecord();
                company.setCompanyName(companyName);
                company.setCompanyNumber(companyNumber);
                company.setDateAdded(dateAdded);
                companies.add(company);
            }
        } finally {
            driver.quit();
        }
        return companies;
    }

    private String lookupAbnByCompanyName(String companyName) {
        // Use Selenium to search ABN Lookup and extract the ABN from the first result
        String abnLookupUrl = "https://abr.business.gov.au/search?q=" + companyName.replace(" ", "+");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get(abnLookupUrl);
            // The ABN is usually in a span with class "search-results__abn"
            List<WebElement> abnElements = driver.findElements(By.cssSelector(".search-results__abn"));
            if (!abnElements.isEmpty()) {
                String abnText = abnElements.get(0).getText();
                // ABN is usually in the format "ABN 12 345 678 901"
                return abnText.replace("ABN", "").replaceAll("\\s+", "");
            }
        } catch (Exception e) {
            log.error("Error looking up ABN for company: {}", companyName, e);
        } finally {
            driver.quit();
        }
        return "";
    }
} 