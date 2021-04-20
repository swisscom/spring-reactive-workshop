package com.swisscom.clouds.workshop.monolith.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "monolith")
public class MonolithProperties {
    private int minLatency = 0;
    private int maxLatency = 0;
    private String petServiceUrl;
}
