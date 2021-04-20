package com.swisscom.clouds.workshop.monolith.controllers;

import com.swisscom.clouds.workshop.monolith.exceptions.UserNotFoundException;
import com.swisscom.clouds.workshop.monolith.repositories.entities.User;
import com.swisscom.clouds.workshop.monolith.services.MonolithService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class MonolithController {

    private final MonolithService monolithService;

    @GetMapping("{userId}")
    public User getUsers(@PathVariable int userId) {
        return monolithService.getUserForId(userId).orElseThrow(() -> {
            throw new UserNotFoundException(userId);
        });
    }

}