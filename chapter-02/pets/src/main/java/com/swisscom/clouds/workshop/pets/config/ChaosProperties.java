package com.swisscom.clouds.workshop.pets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "chaos")
public class ChaosProperties {
    private double exceptionProbability = 0;
}
