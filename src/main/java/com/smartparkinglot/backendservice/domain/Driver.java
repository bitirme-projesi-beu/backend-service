package com.smartparkinglot.backendservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "drivers")
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "password")
    @NonNull @NotNull
    private String password;
    @Column(name = "email")
    @NonNull @NotNull private String email;
    @Column(name = "name")
    @NonNull @NotNull private String name;
    @Column(name="surname")
    @NonNull @NotNull private String surname;
    @NonNull @NotNull LocalDateTime createdAt;
    private Boolean isDeleted;

}