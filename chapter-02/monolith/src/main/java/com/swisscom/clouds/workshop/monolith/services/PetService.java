package com.swisscom.clouds.workshop.monolith.services;

import java.util.Arrays;
import java.util.List;

import com.swisscom.clouds.workshop.monolith.config.MonolithProperties;
import com.swisscom.clouds.workshop.monolith.repositories.entities.Pet;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PetService {

    private final MonolithProperties monolithProperties;
    private final RestTemplate restTemplate;

    public PetService(MonolithProperties monolithProperties) {
        this.monolithProperties = monolithProperties;
        this.restTemplate =  new RestTemplateBuilder().build();
    }

    public List<Pet> getPetsForUserId(int userId) {
        String petServiceUrl = String.format(monolithProperties.getPetServiceUrl(), userId);  
        Pet[] pets = restTemplate.getForObject(petServiceUrl, Pet[].class);
        return Arrays.asList(pets);
    }
}
