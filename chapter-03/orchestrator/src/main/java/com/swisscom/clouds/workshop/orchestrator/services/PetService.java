package com.swisscom.clouds.workshop.orchestrator.services;

import com.swisscom.clouds.workshop.orchestrator.config.OrchestratorProperties;
import com.swisscom.clouds.workshop.orchestrator.services.entities.Pet;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class PetService {

    private final OrchestratorProperties orchestratorProperties;
    private final WebClient webClient;

    public PetService(OrchestratorProperties orchestratorProperties) {
        this.orchestratorProperties = orchestratorProperties;
        this.webClient = WebClient.builder().baseUrl(orchestratorProperties.getPetServiceUrl()).build();
    }

    public Flux<Pet> getPetsForUserId(int userId) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(orchestratorProperties.getPetServicePath())
                        .build(userId))
                .retrieve()
                .bodyToFlux(Pet.class);
    }
}
