package com.swisscom.clouds.workshop.pets.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.swisscom.clouds.workshop.pets.exceptions.DatabaseUnavailableException;
import com.swisscom.clouds.workshop.pets.repositories.PetRepository;
import com.swisscom.clouds.workshop.pets.repositories.entities.Pet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

	private static final int USER_ID =100;
	private static final int PET_ID_1 = 400;
	private static final int PET_ID_2 = 500;

	@Mock 
	private ChaosService chaosService;

	@Mock 
	private PetRepository petRepository;

	@Test
	@DisplayName("Get existing pets by user ID returns Flux with pets")
	public void getAllExistingPestForUserId() {

		Pet pet1 = new Pet();
		pet1.setId(PET_ID_1);
		Pet pet2 = new Pet();
		pet2.setId(PET_ID_2);
		Mockito.when(petRepository.findAllByUserId(USER_ID)).thenReturn(Flux.just(pet1, pet2));		
		PetService petService = new PetService(petRepository, chaosService);

		Flux<Pet> result = petService.getAllPetsForUserId(USER_ID);

		StepVerifier
			.create(result)
			.expectNextMatches(returnedPet -> returnedPet.getId() == PET_ID_1)
			.expectNextMatches(returnedPet -> returnedPet.getId() == PET_ID_2)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Get pets for a non-existing user ID returns empty flux")
	public void getNonExistingPetsForUserId() {

		Mockito.when(petRepository.findAllByUserId(USER_ID)).thenReturn(Flux.empty());		
		PetService petService = new PetService(petRepository, chaosService);
	
		Flux<Pet> result = petService.getAllPetsForUserId(USER_ID);

		StepVerifier
			.create(result)
			.expectComplete()
			.verify();
	}

	@Test
	@DisplayName("Get pets while database is not available")
	public void getPetsWhileDatabaseUnavailable() {

		Mockito.doThrow(DatabaseUnavailableException.class).when(chaosService).checkDatabaseUnavailableException();	
		PetService petService = new PetService(petRepository, chaosService);
	
		assertThrows(DatabaseUnavailableException.class, () -> petService.getAllPetsForUserId(USER_ID));
	}
}