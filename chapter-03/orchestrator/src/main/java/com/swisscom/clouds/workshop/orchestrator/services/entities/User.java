package com.swisscom.clouds.workshop.orchestrator.services.entities;

import java.util.List;

import lombok.Data;

@Data
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;   
    private Integer instanceId;
    private int latency;

    private List<Pet> pets;
}
