package com.swisscom.clouds.workshop.orchestrator.services;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.clouds.workshop.orchestrator.config.OrchestratorProperties;
import com.swisscom.clouds.workshop.orchestrator.exceptions.PetServiceUnavailableException;
import com.swisscom.clouds.workshop.orchestrator.services.entities.Pet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class PetServiceTest {
	
	private static final int USER_ID =100;
	private static final int PET_ID_1 = 400;
	private static final int PET_ID_2 = 500;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static MockWebServer mockBackEnd;
	
	private OrchestratorProperties orchestratorProperties;
	
	@BeforeEach
	public void initialize() throws IOException {
		mockBackEnd = new MockWebServer();
        mockBackEnd.start();

		orchestratorProperties = new OrchestratorProperties();
		orchestratorProperties.setPetServiceUrl(String.format("http://localhost:%s", mockBackEnd.getPort()));
		orchestratorProperties.setPetServicePath("/api/users/{id}/pets");
	}

	@AfterEach
	public void tearDown() throws IOException {
		mockBackEnd.close();
		mockBackEnd.shutdown();
	}	

	@Test
	@DisplayName("Get all existing pets for given user ID")
	public void getAllPetsForUserId() throws IOException {
		Pet pet1 = new Pet();
		pet1.setId(PET_ID_1);
		Pet pet2 = new Pet();
		pet2.setId(PET_ID_2);

		mockBackEnd.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(new Pet[] {pet1, pet2}))
			.addHeader("Content-Type", "application/json"));

		PetService petService = new PetService(orchestratorProperties);
		Flux<Pet> result = petService.getPetsForUserId(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedPet -> returnedPet.getId() == PET_ID_1)
			.expectNextMatches(returnedPet -> returnedPet.getId() == PET_ID_2)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Get no pets returns empty flux")
	public void getNoPets() throws IOException {

		mockBackEnd.enqueue(new MockResponse()
			.setBody("[]")
			.addHeader("Content-Type", "application/json"));

		PetService petService = new PetService(orchestratorProperties);
		Flux<Pet> result = petService.getPetsForUserId(USER_ID);

		StepVerifier
			.create(result)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Retry 2 times in case of 503 error")
	public void retryAfter503() throws IOException {

		Pet pet1 = new Pet();
		pet1.setId(PET_ID_1);
		orchestratorProperties.setPetServiceBackoffDuration(1);
		orchestratorProperties.setPetServiceRetries(2);

		mockBackEnd.enqueue(new MockResponse()
			.setResponseCode(503));

		mockBackEnd.enqueue(new MockResponse()
			.setResponseCode(503));

		mockBackEnd.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(pet1))
			.addHeader("Content-Type", "application/json"));

		PetService petService = new PetService(orchestratorProperties);
		Flux<Pet> result = petService.getPetsForUserId(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedPet -> returnedPet.getId() == PET_ID_1)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Exceed max retry in case of 503 error")
	public void exceedMaxRetry() throws IOException {

		Pet pet1 = new Pet();
		pet1.setId(PET_ID_1);
		orchestratorProperties.setPetServiceBackoffDuration(1);
		orchestratorProperties.setPetServiceRetries(1);

		mockBackEnd.enqueue(new MockResponse()
			.setResponseCode(503));

		mockBackEnd.enqueue(new MockResponse()
			.setResponseCode(503));

		mockBackEnd.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(pet1))
			.addHeader("Content-Type", "application/json"));

		PetService petService = new PetService(orchestratorProperties);
		Flux<Pet> result = petService.getPetsForUserId(USER_ID);

		StepVerifier
			.create(result)
			.expectErrorMatches(throwable -> throwable instanceof PetServiceUnavailableException)
			.verify();
	}

	@Test
	@DisplayName("Retry only for database unavailable exception")
	public void doNotRetryForNonFilteredExceptions() throws IOException {

		Pet pet1 = new Pet();
		pet1.setId(PET_ID_1);
		orchestratorProperties.setPetServiceBackoffDuration(100);
		orchestratorProperties.setPetServiceRetries(2);

		mockBackEnd.enqueue(new MockResponse()
			.setResponseCode(HttpStatus.NOT_FOUND.value()));

		mockBackEnd.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(pet1))
			.addHeader("Content-Type", "application/json"));

		PetService petService = new PetService(orchestratorProperties);
		Flux<Pet> result = petService.getPetsForUserId(USER_ID);

		StepVerifier
			.create(result)
			.expectErrorMatches(throwable -> throwable instanceof WebClientResponseException &&
					((WebClientResponseException) throwable).getStatusCode().equals(HttpStatus.NOT_FOUND))
			.verify();
	}
}
