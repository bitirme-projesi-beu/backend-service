package com.smartparkinglot.backendservice.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull @NotNull private Long parkingLotOwnerId;
    @NonNull @NotNull private String name;
    @NonNull @NotNull private Double hourlyWage;
    @NonNull @NotNull private Integer maxCapacity;
    private Integer activeCapacity;
    @NonNull @NotNull private Double latitude;
    @NonNull @NotNull private Double longtitude;
    private Double rating;
    @NonNull @NotNull private Boolean isDeleted;

}
