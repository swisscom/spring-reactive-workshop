package com.swisscom.clouds.workshop.users.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.swisscom.clouds.workshop.users.config.ChaosProperties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChaosServiceTest {

	@Test
	@DisplayName("Latency for given latency")
	public void checkLatency() {

		int givenLatency = 5000;
		ChaosProperties chaosProperties = new ChaosProperties();
		chaosProperties.setLatencyMax(givenLatency);
		chaosProperties.setLatencyMin(givenLatency);
		ChaosService chaosService = new ChaosService(chaosProperties);

		int calculatedLatency = chaosService.getLatency();

		assertEquals(calculatedLatency, givenLatency);
	}
}
