package com.swisscom.clouds.workshop.monolith.services;

import java.util.Random;

import com.swisscom.clouds.workshop.monolith.config.ChaosProperties;
import com.swisscom.clouds.workshop.monolith.exceptions.DatabaseUnavailableException;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChaosService {

    private final Random random;
    private final ChaosProperties chaosProperties;

    public ChaosService(ChaosProperties chaosProperties) {
        this.random = new Random();
        this.chaosProperties = chaosProperties;
    }

    public void checkDatabaseUnavailableException() {
        if (random.nextDouble() < chaosProperties.getExceptionProbability()) {
            log.error("Database is not available");
            throw new DatabaseUnavailableException();
        }
    }

    public int applyLatency() {
        int latency = calculateLatency();
        try {
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return latency;        
    }

    private int calculateLatency() {
        return random.nextInt(chaosProperties.getMaxLatency() - chaosProperties.getMinLatency() + 1)
                + chaosProperties.getMinLatency();
    }
}
