package com.smartparkinglot.backendservice.repository;

import com.smartparkinglot.backendservice.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByParkingLotId(Long parkingLotId);
    List<Ticket> findByDriverId(Long driverId);
    Ticket findByParkingLotIdAndPlateAndIsItInsideTrue(Long parkingLotId, String plate);
}
