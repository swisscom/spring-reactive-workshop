package com.swisscom.clouds.workshop.users.repositories;

import com.swisscom.clouds.workshop.users.repositories.entities.User;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer>{
       
}
