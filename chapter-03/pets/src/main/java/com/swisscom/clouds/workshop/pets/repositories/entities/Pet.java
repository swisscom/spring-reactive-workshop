package com.swisscom.clouds.workshop.pets.repositories.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table
public class Pet {

    @Id
    private int id;
    
    private String name;
    private String species;
    private int userId;
}