package com.smartparkinglot.backendservice.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull private Long parkingLotOwnerId;
    // future work(owner id) -> for role based auth,, otopark personeli rolüne sahip herkes tüm otoparklara erişemesin diye
        // erişilmeye çalışılan otopark in owner id'si ile erişmeye çalışan kişinin id'si örtüşmeşi
    @NonNull @NotNull private String name;
    @NonNull @NotNull private Double hourlyWage;
    @NonNull @NotNull private Integer maxCapacity;
    private Integer activeCapacity;
    @NonNull @NotNull private Double latitude;
    @NonNull @NotNull private Double longtitude;
    private Double rating;
    @NonNull @NotNull private Boolean isDeleted;

}
