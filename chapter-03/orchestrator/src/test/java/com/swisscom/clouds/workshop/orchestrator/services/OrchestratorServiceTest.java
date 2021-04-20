package com.swisscom.clouds.workshop.orchestrator.services;

import com.swisscom.clouds.workshop.orchestrator.services.entities.Pet;
import com.swisscom.clouds.workshop.orchestrator.services.entities.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(MockitoExtension.class)
public class OrchestratorServiceTest {

	private static final int USER_ID = 100;
	private static final int PET_ID_1 = 400;
	private static final int PET_ID_2 = 400;

	@Mock
	PetService petService;

	@Mock
	UserService userService;


	@Test
	public void getUserAndPetsForUserId() {
		User user = new User();
		user.setId(USER_ID);

		Pet pet1 = new Pet();
		pet1.setId(PET_ID_1);

		Pet pet2 = new Pet();
		pet2.setId(PET_ID_2);

		Mockito.when(userService.getUserById(USER_ID)).thenReturn(Mono.just(user));
		Mockito.when(petService.getPetsForUserId(USER_ID)).thenReturn(Flux.just(pet1, pet2));

		OrchestratorService orchestratorService = new OrchestratorService(userService, petService);
		Mono<User> result = orchestratorService.getUserWithPetsForId(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(this::checkUser)
			.expectComplete()
			.verify();
			
	}

	private boolean checkUser(User user) {
		return user.getId() == USER_ID && 
			user.getPets().size() == 2 && 
			user.getPets().get(0).getId() == PET_ID_1 && 
			user.getPets().get(1).getId() == PET_ID_2;
	}
}
