package com.swisscom.clouds.workshop.monolith.services;

import java.util.Optional;

import com.swisscom.clouds.workshop.monolith.exceptions.UserNotFoundException;
import com.swisscom.clouds.workshop.monolith.repositories.MonolithRepository;
import com.swisscom.clouds.workshop.monolith.repositories.entities.User;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonolithService {

    private final MonolithRepository monolithRepository;
    private final ChaosService chaosService;
    private final PetService petService;

    public User getUserForId(int userId) {

        //
        // The monolith is still slow but we know now
        // that the exceptions came from the pets. 
        //
        int latency = chaosService.applyLatency();

        Optional<User> userOptional = monolithRepository.findById(userId);
        return userOptional
                .map(user -> this.setLatency(user, latency))
                .map(this::addPetsForUser)
                .orElseThrow(() -> {
                    throw new UserNotFoundException(userId);
                });
    }

    private User setLatency(User user, int latency) {
        user.setLatency(latency);
        return user;
    }

    private User addPetsForUser(User user) {
        user.setPets(petService.getPetsForUserId(user.getId()));;
        return user;
    }

}
