package com.smartparkinglot.backendservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "reservations")
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private Long parkingLotId;
    @NonNull
    private Long ownerId;
    private boolean isActive;
    @NonNull
    private Date createdAt;
    @NonNull
    private String plate;
}
