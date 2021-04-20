package com.swisscom.clouds.workshop.monolith.services;

import java.util.Random;

import com.swisscom.clouds.workshop.monolith.config.MonolithProperties;

import org.springframework.stereotype.Service;

@Service
public class ChaosService {

    private final Random random;
    private final MonolithProperties monolithProperties;

    public ChaosService(MonolithProperties monolithProperties) {
        this.random = new Random();
        this.monolithProperties = monolithProperties;
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
        return random.nextInt(monolithProperties.getMaxLatency() - monolithProperties.getMinLatency() + 1)
                + monolithProperties.getMinLatency();
    }
}
