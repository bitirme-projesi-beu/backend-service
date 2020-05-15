package com.smartparkinglot.backendservice.repository;

import com.smartparkinglot.backendservice.domain.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot,Long> {
    ParkingLot findByName(String name);
}
