package com.smartparkinglot.backendservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name = "reservations")
@NoArgsConstructor
@RequiredArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull @NotNull private Long parkingLotId;
    @NonNull @NotNull private Long driverId;
    private boolean isActive;
    @NonNull @NotNull private Date createdAt;
    @NonNull @NotNull private String plate;
    }
