package com.swisscom.clouds.workshop.monolith.repositories.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class User {

    @Id
    private int id;

    private String firstName;
    private String lastName;
    private String email;    

    @Transient
    private int latency;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    private List<Pet> pets = new ArrayList<>();
}
