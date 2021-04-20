package com.swisscom.clouds.workshop.pets.repositories;

import com.swisscom.clouds.workshop.pets.repositories.entities.Pet;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface PetRepository extends ReactiveCrudRepository<Pet, Integer> {
    public Flux<Pet> findAllByUserId(int userId);
}
