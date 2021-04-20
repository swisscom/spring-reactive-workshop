package com.swisscom.clouds.workshop.monolith.services;

import java.util.Optional;

import com.swisscom.clouds.workshop.monolith.repositories.MonolithRepository;
import com.swisscom.clouds.workshop.monolith.repositories.entities.User;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonolithService {

    private final MonolithRepository monolithRepository;
    private final ChaosService chaosService;

    public Optional<User> getUserForId(int userId) {

        //
        // The monolith is slow and sometimes also
        // throws an error. We really should start
        // analyzing where things go wrong!
        //
        int latency = chaosService.applyLatency();
        chaosService.checkDatabaseUnavailableException();

        return monolithRepository.findById(userId).map(user -> setLatency(user, latency));
    }

    private User setLatency(User user, int latency) {
        user.setLatency(latency);
        return user;
    }
}
