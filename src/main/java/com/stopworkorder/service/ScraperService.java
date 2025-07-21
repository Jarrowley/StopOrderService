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
                
                String companyNumber = "ABN"; //lookupAbnByCompanyName(companyName);
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
        String abnLookupUrl = "https://abr.business.gov.au/Search/ResultsActive?SearchText=" + companyName.replace(" ", "+");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get(abnLookupUrl);
            // Find all table rows in the results
            List<WebElement> rows = driver.findElements(By.tagName("tr"));
            for (WebElement row : rows) {
                List<WebElement> tds = row.findElements(By.tagName("td"));
                if (tds.size() < 2) continue; // Need at least company and ABN columns
                boolean foundCompany = false;
                String abn = "";
                for (WebElement td : tds) {
                    String tdText = td.getText().trim();
                    if (tdText.equalsIgnoreCase(companyName)) {
                        foundCompany = true;
                    }
                }
                if (foundCompany) {
                    // Try to find ABN in the same row
                    for (WebElement td : tds) {
                        String tdText = td.getText().trim();
                        if (tdText.matches("^(ABN\\s*)?\\d{2}\\s?\\d{3}\\s?\\d{3}\\s?\\d{3}$")) {
                            abn = tdText.replace("ABN", "").replaceAll("\\s+", "");
                            return abn;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error looking up ABN for company: {}", companyName, e);
        } finally {
            driver.quit();
        }
        return "";
    }
} 