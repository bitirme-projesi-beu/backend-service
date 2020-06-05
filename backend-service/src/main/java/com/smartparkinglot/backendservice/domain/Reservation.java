package com.smartparkinglot.backendservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NonNull private Long driverId;
    private boolean isActive;
    @NonNull @NotNull private LocalDateTime createdAt;
    private LocalDateTime deactivatedAt;
    @NonNull @NotNull private String plate;
    @NonNull  private Double hourlyWage;
    private Double cost;
}