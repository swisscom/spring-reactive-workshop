package com.swisscom.clouds.workshop.users.services;

import com.swisscom.clouds.workshop.users.repositories.UserRepository;
import com.swisscom.clouds.workshop.users.repositories.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	private final static int LATENCY = 50;
	private final static int USER_ID = 200;

	@Mock
	private Environment env;

	@Mock 
	private ChaosService chaosService;

	@Mock 
	private UserRepository userRepository;

	private UserService userService;

	@BeforeEach
	public void setup() {
		Mockito.when(env.getProperty("server.port", "8080")).thenReturn("8089");
		Mockito.when(chaosService.getLatency()).thenReturn(LATENCY);
		User user = new User();
		user.setId(USER_ID);
		Mockito.when(userRepository.findById(USER_ID)).thenReturn(Mono.just(user));		
		userService = new UserService(env, userRepository, chaosService);
	}

	@Test
	@DisplayName("Get existing user by ID returns Mono with user")
	public void getExistingUserById() {

		Mono<User> result = userService.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedUser -> returnedUser.getId() == USER_ID)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Get user by non-existing ID returns empty mono")
	public void getNonExistingUserById() {

		Mockito.when(userRepository.findById(USER_ID)).thenReturn(Mono.empty());		
		UserService userService = new UserService(env, userRepository, chaosService);

		Mono<User> result = userService.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Existing user has correct latency information set")
	public void getExistingUserByIdWithCorrectLatency() {

		Mono<User> result = userService.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedUser -> returnedUser.getLatency() == LATENCY)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Existing user has correct instance id set")
	public void getExistingUserByIdWithCorrectInstanceId() {

		Mono<User> result = userService.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedUser -> returnedUser.getInstanceId() == 9)
			.expectComplete()
			.verify();
	}
}
