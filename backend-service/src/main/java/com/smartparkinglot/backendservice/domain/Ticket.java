package com.smartparkinglot.backendservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="tickets")
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private Long parkingLotId;
    @NonNull
    private Long driverId; // in case of guest it will be -1
    @NonNull
    private String plate;
    private LocalDateTime createdAt;
    private LocalDateTime exitedAt;
    @NonNull
    private Boolean isItInside;
    private Double cost;



    public void addToTheCost(Double ticketCost){
        if (cost == null ) cost = 0.0;
        else{
            cost += ticketCost;
        }
    }
}
