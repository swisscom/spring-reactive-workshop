package com.swisscom.clouds.workshop.orchestrator.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "orchestrator")
public class OrchestratorProperties {
    private String userServiceUrl;
    private List<Integer> userServicePorts;
    private String userServicePath;
    private String petServiceUrl;
    private String petServicePath;
    private int petServiceRetries = 1;
    private int petServiceBackoffDuration = 100;
}