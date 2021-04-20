package com.swisscom.clouds.workshop.orchestrator.services;

import java.util.List;

import com.swisscom.clouds.workshop.orchestrator.services.entities.Pet;
import com.swisscom.clouds.workshop.orchestrator.services.entities.User;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrchestratorService {

    private final UserService userService;
    private final PetService petService;

    public Mono<User> getUserWithPetsForId(int userId) {
        return userService
            .getUserById(userId)
            .zipWith(getPetsForUserId(userId))
            .map(tuple -> setPetListForUser(tuple.getT1(), tuple.getT2()));
    }

    private Mono<List<Pet>> getPetsForUserId(int userId) {
        return petService
                .getPetsForUserId(userId)
                .collectList();
    }

    private User setPetListForUser(User user, List<Pet> petList) {
        user.setPets(petList);
        return user;
    }
}