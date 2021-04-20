package com.swisscom.clouds.workshop.orchestrator.controllers;

import com.swisscom.clouds.workshop.orchestrator.services.OrchestratorService;
import com.swisscom.clouds.workshop.orchestrator.services.entities.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class OrchestratorController {

    private final OrchestratorService orchestratorService;

    @GetMapping("{userId}")
    public Mono<User> getUserById(@PathVariable int userId) {
        log.info("--> Start new Orchestrator Controller request with user id = {}", userId);

        return orchestratorService
                .getUserWithPetsForId(userId)
                .log(getClass().getName());
    }
}
