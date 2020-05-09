package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReservationService {
    ResponseEntity<List<Reservation>> listAll();
    ResponseEntity<List<Reservation>> getDriverReservations(Long owner_id);
    ResponseEntity<Reservation> getReservation(String plate);
    ResponseEntity<Reservation> addOrSave(Reservation reservation);
    ResponseEntity deleteById(Long reservation_id);
    Boolean checkActiveReservation();
}
