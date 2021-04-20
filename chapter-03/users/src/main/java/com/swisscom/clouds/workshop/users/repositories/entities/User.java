package com.swisscom.clouds.workshop.users.repositories.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table
public class User {

    @Id
    private int id;

    private String firstName;
    private String lastName;
    private String email;   

    @Transient
    private Integer instanceId;
    
    @Transient
    private int latency;

}
