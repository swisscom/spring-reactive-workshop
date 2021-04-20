package com.swisscom.clouds.workshop.orchestrator.services;

import com.swisscom.clouds.workshop.orchestrator.config.OrchestratorProperties;
import com.swisscom.clouds.workshop.orchestrator.exceptions.UserNotFoundException;
import com.swisscom.clouds.workshop.orchestrator.services.entities.User;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final OrchestratorProperties orchestratorProperties;
    private final WebClient webClient;
    
    public UserService(OrchestratorProperties orchestratorProperties, WebClient.Builder webClientBuilder) {
        this.orchestratorProperties = orchestratorProperties;
        this.webClient = 
                webClientBuilder
                    .baseUrl(orchestratorProperties.getUserServiceUrl())
                    .build();
    }

    public Mono<User> getUserById(int userId) {
        return Flux.fromIterable(orchestratorProperties.getUserServicePorts())
                .flatMap(baseUrl -> queryBackend(baseUrl, userId))
                .next();
    }

    private Mono<User> queryBackend(int port, int userId) {
        return webClient.get()
                .uri(uriBuilder -> 
                        uriBuilder
                            .port(port)
                            .path(orchestratorProperties.getUserServicePath())
                            .build(userId))
                .retrieve()
                .onStatus(httpStatus -> 
                            HttpStatus.NOT_FOUND.equals(httpStatus),    
                            clientResponse -> Mono.error(new UserNotFoundException(userId)))
                .bodyToMono(User.class);
    }
}

