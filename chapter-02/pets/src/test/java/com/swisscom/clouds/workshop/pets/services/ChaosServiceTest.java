package com.swisscom.clouds.workshop.pets.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.swisscom.clouds.workshop.pets.config.ChaosProperties;
import com.swisscom.clouds.workshop.pets.exceptions.DatabaseUnavailableException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChaosServiceTest {

	@Test
	@DisplayName("Exception probability zero results in no Database Unavailable Exception")
	public void checkExceptionProbabilityZero() {

		ChaosProperties chaosProperties = new ChaosProperties();
		chaosProperties.setExceptionProbability(0);
		ChaosService chaosService = new ChaosService(chaosProperties);

		assertDoesNotThrow(() -> chaosService.checkDatabaseUnavailableException());
	}

	@Test
	@DisplayName("Exception probability one results in Database Unavailable Exception thrown")
	public void checkExceptionProbabilityOne() {

		ChaosProperties chaosProperties = new ChaosProperties();
		chaosProperties.setExceptionProbability(1);
		ChaosService chaosService = new ChaosService(chaosProperties);

		assertThrows(DatabaseUnavailableException.class, () -> chaosService.checkDatabaseUnavailableException());
	}

}
