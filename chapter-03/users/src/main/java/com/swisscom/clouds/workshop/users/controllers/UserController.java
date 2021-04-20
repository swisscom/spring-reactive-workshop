package com.swisscom.clouds.workshop.users.controllers;

import com.swisscom.clouds.workshop.users.repositories.entities.User;
import com.swisscom.clouds.workshop.users.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public Mono<User> getUserById(@PathVariable int userId) {
        log.info("--> Start new User Controller request with user id = {}", userId);

        return userService
                    .getUserById(userId)
                    .log(getClass().getName());
    }
}
