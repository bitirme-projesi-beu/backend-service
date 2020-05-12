package com.smartparkinglot.backendservice.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "drivers")
@NoArgsConstructor
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

    @JsonManagedReference
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("createdAt")
    private List<Reservation> reservationList;

    private Boolean isDeleted;
}