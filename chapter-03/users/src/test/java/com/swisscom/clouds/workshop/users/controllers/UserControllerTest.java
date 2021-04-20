package com.swisscom.clouds.workshop.users.controllers;

import com.swisscom.clouds.workshop.users.exceptions.UserNotFoundException;
import com.swisscom.clouds.workshop.users.repositories.entities.User;
import com.swisscom.clouds.workshop.users.services.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	private static final int USER_ID = 200;
	
	@Mock
	private UserService userService;

	@Test
	@DisplayName("Get existing user by ID returns Mono with user")
	public void getExistingUserById() {

		User user = new User();
		user.setId(USER_ID);
		Mockito.when(userService.getUserById(USER_ID)).thenReturn(Mono.just(user));		

		UserController userController = new UserController(userService);
		Mono<User> result = userController.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedUser -> user.getId() == USER_ID)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Get a non-existing user by ID throws a UserNotFoundException")
	public void getNonExistentUserThrowsUserotFoundException() {

		Mockito.when(userService.getUserById(USER_ID)).thenReturn(Mono.empty());		

		UserController userController = new UserController(userService);
		Mono<User> result = userController.getUserById(USER_ID);

		StepVerifier
			.create(result)
			.expectErrorMatches(throwable -> throwable instanceof UserNotFoundException)
			.verify();
	}
}
