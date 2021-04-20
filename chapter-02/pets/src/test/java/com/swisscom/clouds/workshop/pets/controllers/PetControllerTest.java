package com.swisscom.clouds.workshop.pets.controllers;

import com.swisscom.clouds.workshop.pets.repositories.entities.Pet;
import com.swisscom.clouds.workshop.pets.services.PetService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

	private static final int USER_ID =100;
	private static final int PET_ID_1 = 400;
	private static final int PET_ID_2 = 500;
	
	@Mock
	private PetService petService;

	@Test
	@DisplayName("Get all existing pets for user ID returns Flux with pets")
	public void getExistingUserById() {

		Pet pet1 = new Pet();
		pet1.setId(PET_ID_1);
		Pet pet2 = new Pet();
		pet2.setId(PET_ID_2);
		Mockito.when(petService.getAllPetsForUserId(USER_ID)).thenReturn(Flux.just(pet1, pet2));		
		
		PetController petController = new PetController(petService);
		Flux<Pet> result = petController.getAllPetsForUserId(USER_ID);	

		StepVerifier
			.create(result)
			.expectNextMatches(returnedPet -> returnedPet.getId() == PET_ID_1)
			.expectNextMatches(returnedPet -> returnedPet.getId() == PET_ID_2)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Get no pets returns empty flux")
	public void getNoPets() {

		Mockito.when(petService.getAllPetsForUserId(USER_ID)).thenReturn(Flux.empty());		
		
		PetController petController = new PetController(petService);
		Flux<Pet> result = petController.getAllPetsForUserId(USER_ID);	

		StepVerifier
			.create(result)
			.expectComplete()
			.verify();
	}
}
