package com.swisscom.clouds.workshop.users.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "chaos")
public class ChaosProperties {
    private int latencyMin = 0;
    private int latencyMax = 0;
}
