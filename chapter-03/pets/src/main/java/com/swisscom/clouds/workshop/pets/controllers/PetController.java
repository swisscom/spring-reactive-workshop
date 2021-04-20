package com.swisscom.clouds.workshop.pets.controllers;

import com.swisscom.clouds.workshop.pets.repositories.entities.Pet;
import com.swisscom.clouds.workshop.pets.services.PetService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("users/{userId}/pets")
    public Flux<Pet> getAllPetsForUserId(@PathVariable int userId) {
        log.info("--> Start new Pet Controller request with user id = {}", userId);

        return petService
                .getAllPetsForUserId(userId)
                .log(getClass().getName());
    }
}