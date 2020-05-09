package com.smartparkinglot.backendservice.repositories;

import com.smartparkinglot.backendservice.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Reservation findByPlate(String plate);
    void deleteById(Long id);
}
