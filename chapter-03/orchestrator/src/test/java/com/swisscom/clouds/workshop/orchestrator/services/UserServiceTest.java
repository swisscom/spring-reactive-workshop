package com.swisscom.clouds.workshop.orchestrator.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.clouds.workshop.orchestrator.config.OrchestratorProperties;
import com.swisscom.clouds.workshop.orchestrator.exceptions.UserNotFoundException;
import com.swisscom.clouds.workshop.orchestrator.services.entities.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UserServiceTest {

	private static final int USER_ID = 100;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static MockWebServer mockBackEnd;
	
	private OrchestratorProperties orchestratorProperties;
	
	@BeforeEach
	public void initialize() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();

		orchestratorProperties = new OrchestratorProperties();
		orchestratorProperties.setUserServiceUrl("http://localhost");
		orchestratorProperties.setUserServicePorts(Arrays.asList(mockBackEnd.getPort()));
		orchestratorProperties.setUserServicePath("/api/users/{id}");
	}

	
    @AfterEach
    public void tearDown() throws IOException {
		mockBackEnd.close();
        mockBackEnd.shutdown();
    }

	@Test
	@DisplayName("Get user for given user ID")
	public void getUserById() throws IOException {

		User user = new User();
		user.setId(USER_ID);

		mockBackEnd.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(user))
			.addHeader("Content-Type", "application/json"));

		UserService userService = new UserService(orchestratorProperties, WebClient.builder());
		Mono<User> result = userService.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedUser -> returnedUser.getId() == USER_ID)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Get user with non-exiting user id")
	public void getNonExistingUserId() throws IOException {

		mockBackEnd.enqueue(new MockResponse()
			.setBody("")
			.setResponseCode(HttpStatus.NOT_FOUND.value())
			.addHeader("Content-Type", "application/json"));

		UserService userService = new UserService(orchestratorProperties, WebClient.builder());
		Mono<User> result = userService.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectErrorMatches(throwable -> throwable instanceof UserNotFoundException)
			.verify();
	}

	@Test
	@DisplayName("Use fastest reply ")
	public void useFastestReply() throws IOException {

		User user1 = new User();
		user1.setId(USER_ID);
		user1.setLastName("First");
		User user2 = new User();
		user2.setId(USER_ID);
		user2.setLastName("Second");

		final MockWebServer mockBackEnd2 = new MockWebServer();
        mockBackEnd2.start();

		orchestratorProperties.setUserServicePorts(
			Arrays.asList(
				orchestratorProperties.getUserServicePorts().get(0),
				mockBackEnd2.getPort()
			)
		);

		mockBackEnd.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(user1))
			.setBodyDelay(200, TimeUnit.MILLISECONDS)
			.addHeader("Content-Type", "application/json"));

		mockBackEnd2.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(user2))
			.addHeader("Content-Type", "application/json"));

		UserService userService = new UserService(orchestratorProperties, WebClient.builder());
		Mono<User> result = userService.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedUser -> returnedUser.getLastName().equals("Second"))
			.expectComplete()
			.verify();

		mockBackEnd2.close();
		mockBackEnd2.shutdown();
	}
}
