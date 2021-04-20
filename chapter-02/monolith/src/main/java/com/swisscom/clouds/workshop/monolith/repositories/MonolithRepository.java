package com.swisscom.clouds.workshop.monolith.repositories;

import com.swisscom.clouds.workshop.monolith.repositories.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonolithRepository extends JpaRepository<User, Integer> {
}
