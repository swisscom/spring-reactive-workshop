package com.swisscom.clouds.workshop.orchestrator.services.entities;

import lombok.Data;

@Data
public class Pet {
    private int id;
    private String name;
    private String species;
    private int userId;
}
