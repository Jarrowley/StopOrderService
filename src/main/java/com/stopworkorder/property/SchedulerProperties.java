package com.stopworkorder.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "notification")
public class SchedulerProperties {
    private String webUrl;
    private String cron;
    private List<String> recipients;
} 