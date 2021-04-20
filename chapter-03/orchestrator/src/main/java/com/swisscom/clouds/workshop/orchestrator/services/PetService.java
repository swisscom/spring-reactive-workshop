package com.swisscom.clouds.workshop.orchestrator.services;

import java.time.Duration;

import com.swisscom.clouds.workshop.orchestrator.config.OrchestratorProperties;
import com.swisscom.clouds.workshop.orchestrator.exceptions.PetServiceUnavailableException;
import com.swisscom.clouds.workshop.orchestrator.services.entities.Pet;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

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
                .bodyToFlux(Pet.class)
                .retryWhen(getRetryStrategy());
    }

    private RetryBackoffSpec getRetryStrategy() {
        return Retry
                .backoff(
                    orchestratorProperties.getPetServiceRetries(),
                    Duration.ofMillis(orchestratorProperties.getPetServiceBackoffDuration()))
                .filter(this::is503ServiceUnavailableError)
                .onRetryExhaustedThrow((retrySpec, retrySignal) -> new PetServiceUnavailableException())
                .doBeforeRetry(retrySignal -> {
                    log.warn("Retry #{} to fetch Pets due to error: {}", 
                    retrySignal.totalRetries(), 
                    retrySignal.failure());
                });
    }

    private boolean is503ServiceUnavailableError(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
