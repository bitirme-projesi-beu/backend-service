package com.smartparkinglot.backendservice.controllers;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.services.reservationservice.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reservation")
@AllArgsConstructor
public class ReservationController {

    ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(){
        return reservationService.listAll();
    }

    @GetMapping("{owner_id}/")
    public ResponseEntity<List<Reservation>> getDriverReservations(@PathVariable Long owner_id){
        return reservationService.getDriverReservations(owner_id);
    }

    @GetMapping("{owner_id}/{plate}")
    public ResponseEntity<Reservation> getDriverReservation(@PathVariable Long owner_id, @PathVariable String plate){
        return reservationService.getReservation(plate);
    }

    @PostMapping
    public ResponseEntity<Reservation> addOrSave(Reservation reservation){
        return reservationService.addOrSave(reservation);
    }

    @DeleteMapping
    public ResponseEntity deleteReservation(Long reservation_id){
        return reservationService.deleteById(reservation_id);
    }
}
