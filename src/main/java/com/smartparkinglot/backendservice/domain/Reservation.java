package com.smartparkinglot.backendservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    @OrderBy("id")
    private Driver owner;
    private boolean isActive;
    @NonNull
    private Date createdAt;
    @NonNull
    private String plate;
    @NonNull
    private Boolean isDeleted;
}
