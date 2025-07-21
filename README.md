# StopWorkOrder

This application scrapes the NSW Building Commission's stop work orders page, extracts company information, looks up the ABN, stores new entries in a database, and sends email notifications to configured recipients.

## Prerequisites
- Java 21+
- Chrome/Chromium browser and ChromeDriver installed and available in your PATH
- Maven (for building the jar)

## Build the Application

```
mvn clean package
```

This will generate a jar file in the `target/` directory, e.g.:

```
target/StopWorkOrder-0.0.1-SNAPSHOT.jar
```

## Prepare Your `application.yml`

Create an `application.yml` file in the same directory where you will run the jar. Example:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

mail:
  host: smtp.gmail.com # or your SMTP host
  port: 587 # or your SMTP port
  username: your_email@gmail.com
  password: your_app_password
  properties:
    mail:
      smtp:
        auth: true
        starttls:
          enable: true

notification:
  cron: "0 * * * * *" # every minute for testing
  recipients:
    - recipient1@example.com
    - recipient2@example.com
  email-template: |
    A new company has been added to the liquidation list.

    Company Name: {{companyName}}
    Company Number: {{companyNumber}}
    Date Added: {{dateAdded}}
```

**Required fields:**
- `mail.host`: SMTP server host (e.g., `smtp.gmail.com`)
- `mail.port`: SMTP server port (e.g., `587`)
- `mail.username`: SMTP username (your email address)
- `mail.password`: SMTP password or app password
- `notification.recipients`: List of email addresses to notify

## Run the Application

From the directory containing your `application.yml` and the jar file:

```
java -jar target/StopWorkOrder-0.0.1-SNAPSHOT.jar --spring.config.location=./application.yml
```

- The application will use the external `application.yml` for configuration.
- Emails will be sent to the recipients when a new company is detected.

## Notes
- Ensure ChromeDriver is installed and matches your Chrome/Chromium version.
- For Gmail, you must use an [App Password](https://support.google.com/accounts/answer/185833?hl=en) if 2FA is enabled.
- The ABN is scraped from the public ABN Lookup site; this may be slow and is subject to change if the site layout changes.
- The database is in-memory (H2) and will reset on each run.

## Troubleshooting
- If you see errors about missing ChromeDriver, install it and ensure it's in your PATH.
- If emails are not sent, check your SMTP credentials and network/firewall settings.
- If ABN scraping fails, check the ABN Lookup site manually for changes. 