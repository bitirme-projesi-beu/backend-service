package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> listAll();
    Reservation getReservation(Long id);
    Reservation getReservation(String plate);
    Reservation addOrSave(Reservation reservation);
    void deleteById(Reservation reservation);
    Boolean checkActiveReservation(Long id);
}
