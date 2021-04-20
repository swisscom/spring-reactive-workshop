package com.swisscom.clouds.workshop.pets.services;

import com.swisscom.clouds.workshop.pets.repositories.PetRepository;
import com.swisscom.clouds.workshop.pets.repositories.entities.Pet;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final ChaosService chaosService;

    public Flux<Pet> getAllPetsForUserId(int userId) {

        //
        // The unreliable network still sometimes results in
        // an unavailable database
        //
        return petRepository
                .findAllByUserId(userId)
                .doFirst(() -> chaosService.checkDatabaseUnavailableException());
    }
}
