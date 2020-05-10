package com.smartparkinglot.backendservice.controllers;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.services.reservationservice.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<List<Reservation>>(reservationService.listAll(),HttpStatus.FOUND);
    }

    @GetMapping("{owner_id}/")
    public ResponseEntity<List<Reservation>> getDriverReservations(@PathVariable Long owner_id){
        return new ResponseEntity<List<Reservation>>(reservationService.getDriverReservations(owner_id),HttpStatus.FOUND);
    }

    @GetMapping("{owner_id}/{plate}")
    public ResponseEntity<Reservation> getDriverReservation(@PathVariable Long owner_id, @PathVariable String plate){
        return new ResponseEntity<Reservation>(reservationService.getReservation(plate),HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Reservation> addOrSave(Reservation reservation){
        return new ResponseEntity<Reservation>(reservationService.addOrSave(reservation),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity deleteReservation(Long reservation_id){
        reservationService.deleteById(reservation_id);
        return new ResponseEntity( HttpStatus.OK);
    }
}
