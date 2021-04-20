package com.swisscom.clouds.workshop.pets.services;

import java.util.Random;

import com.swisscom.clouds.workshop.pets.config.ChaosProperties;
import com.swisscom.clouds.workshop.pets.exceptions.DatabaseUnavailableException;

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
}
