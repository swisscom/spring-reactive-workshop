package com.swisscom.clouds.workshop.users.services;

import java.util.Random;

import com.swisscom.clouds.workshop.users.config.ChaosProperties;

import org.springframework.stereotype.Service;

@Service
public class ChaosService {

    private final Random random;
    private final ChaosProperties chaosProperties;

    public ChaosService(ChaosProperties chaosProperties) {
        this.random = new Random();
        this.chaosProperties = chaosProperties;
    }

    public int getLatency() {
        return random.nextInt(
            chaosProperties.getLatencyMax() - chaosProperties.getLatencyMin() + 1)
                + chaosProperties.getLatencyMin();
    }
}
