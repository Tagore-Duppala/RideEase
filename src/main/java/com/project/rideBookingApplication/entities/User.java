package com.project.rideBookingApplication.entities;

import com.project.rideBookingApplication.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.LAZY) //creates a seperate table as the field is a collection
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


}
