package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReservationService {
    List<Reservation> listAll();
    List<Reservation> getDriverReservations(Long owner_id);
    Reservation getReservation(String plate);
    Reservation addOrSave(Reservation reservation);
    void deleteById(Long reservation_id);
    Boolean checkActiveReservation();
}
