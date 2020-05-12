package com.smartparkinglot.backendservice.controllers;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.services.reservationservice.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(){
        return new ResponseEntity<List<Reservation>>(reservationService.listAll(),HttpStatus.FOUND);
    }

    @GetMapping("id/{res_id}")
    public ResponseEntity<Reservation> getReservationWithResId(@PathVariable Long res_id){
        return new ResponseEntity<Reservation>(reservationService.getReservation(res_id),HttpStatus.FOUND);
    }

    @GetMapping("plate/{plate}")
    public ResponseEntity<Reservation> getDriverReservation(@PathVariable String plate){
        return new ResponseEntity<Reservation>(reservationService.getReservation(plate),HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Reservation> addOrSave(@RequestBody Reservation reservation){
        return new ResponseEntity<Reservation>(reservationService.addOrSave(reservation),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity deleteReservation(@RequestBody Reservation reservation){
        reservationService.delete(reservation);
        return new ResponseEntity( HttpStatus.OK);
    }
}
