package com.smartparkinglot.backendservice.controller;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.service.reservationservice.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reservations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(){
        return new ResponseEntity<List<Reservation>>(reservationService.listAll(),HttpStatus.FOUND);
    }

    @GetMapping("id/{res_id}")
    public ResponseEntity<Reservation> getReservationWithResId(@PathVariable Long res_id){
        return new ResponseEntity<Reservation>(reservationService.getReservationWithId(res_id),HttpStatus.FOUND);
    }

    @GetMapping("plate/{plate}")
    public ResponseEntity<List<Reservation>> getReservationWithPlate(@PathVariable String plate){
        return new ResponseEntity<List<Reservation>>(reservationService.getReservationWithPlate(plate),HttpStatus.FOUND);
    }

    @GetMapping("drivers/{driver_id}/{res_id}")
    public ResponseEntity<Reservation> getDriverReservation(@PathVariable Long driver_id,@PathVariable Long res_id){
        return new ResponseEntity<Reservation>(reservationService.getDriverReservation(driver_id,res_id),HttpStatus.FOUND);
    }

    @GetMapping("drivers/{driver_id}")
    public ResponseEntity<List<Reservation>> getDriverReservations(@PathVariable Long driver_id){
        return new ResponseEntity<List<Reservation>>(reservationService.getDriverReservations(driver_id),HttpStatus.FOUND);
    }
    @PostMapping
    public ResponseEntity<Reservation> addOrSave(@RequestBody Reservation reservation){
        return new ResponseEntity<Reservation>(reservationService.addOrSave(reservation),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity deleteReservation(@RequestBody Reservation reservation){
        reservationService.deleteById(reservation);
        return new ResponseEntity( HttpStatus.OK);
    }
}
