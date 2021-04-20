package com.swisscom.clouds.workshop.monolith.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "chaos")
public class ChaosProperties {
    private double exceptionProbability = 0;
    private int minLatency = 0;
    private int maxLatency = 0;
}
