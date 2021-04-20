package com.swisscom.clouds.workshop.orchestrator;

import com.swisscom.clouds.workshop.orchestrator.config.OrchestratorProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(OrchestratorProperties.class)
public class OrchestratorApplication {

	public static void main(String[] args) {

		Flux<Integer> myFlux = 
			Flux.just(1, 2, 3)
				.doOnEach(value -> log.info("Current step is {}", value));

		log.info("I am already here");

		myFlux.subscribe(result -> log.info("Result = {}", result));

		SpringApplication.run(OrchestratorApplication.class, args);
	}

}
