package com.swisscom.clouds.workshop.users.services;

import java.time.Duration;

import com.swisscom.clouds.workshop.users.repositories.UserRepository;
import com.swisscom.clouds.workshop.users.repositories.entities.User;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final int instanceId;
    private final UserRepository userRepository;
    private final ChaosService chaosService;

    public UserService(Environment environment, UserRepository userRepository, ChaosService chaosService) {
        int serverPort = Integer.parseInt(environment.getProperty("server.port", "8080"));
        this.instanceId = serverPort % 10;
        this.userRepository = userRepository;
        this.chaosService = chaosService;
    }

    public Mono<User> getUserById(int userId) {

        //
        // This service is just slow ...
        //
        int latency = chaosService.getLatency();

        return userRepository.findById(userId)
            .delayElement(Duration.ofMillis(latency));
    }

    private User setInstanceIdAndLatency(User user, int latency) {
        user.setInstanceId(instanceId);
        user.setLatency(latency);
        return user;
    }
}
