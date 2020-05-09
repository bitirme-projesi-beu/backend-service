package com.smartparkinglot.backendservice.domain;


import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "password")
    @NonNull
    private String password;
    @Column(name = "email")
    @NonNull
    private String email;
    @Column(name = "name")
    @NonNull
    private String name;
    @Column(name="surname")
    @NonNull
    private String surname;





}
