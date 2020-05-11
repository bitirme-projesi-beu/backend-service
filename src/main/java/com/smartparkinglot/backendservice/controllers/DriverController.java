package com.smartparkinglot.backendservice.controllers;


import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.services.driverservice.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class DriverController {
    @Autowired
    private DriverService userService;
    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers(){
        return new ResponseEntity<List<Driver>>(userService.getAllDrivers(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getById(@PathVariable Long id){
        return new ResponseEntity<Driver>(userService.getById(id),HttpStatus.OK);
    }

    @GetMapping("{driver_id}/reservations")
    public ResponseEntity<List<Reservation>> getDriverReservations(@PathVariable Long driver_id){
        return new ResponseEntity<List<Reservation>>(userService.getDriverReservations(driver_id),HttpStatus.FOUND);
    }

    @GetMapping("{driver_id}/reservations/{reservation_id}")
    public ResponseEntity<Reservation> getDriverReservation(@PathVariable Long driver_id, @PathVariable Long reservation_id){
        return new ResponseEntity<Reservation>(userService.getDriverReservation(reservation_id),HttpStatus.FOUND);
    }

    @DeleteMapping("delete")
    public ResponseEntity deleteDriver(@RequestBody Driver driver){
        userService.deleteDriver(driver);
        return new ResponseEntity(HttpStatus.OK);
    }
    @DeleteMapping()
    public ResponseEntity deactiveDriver(@RequestBody Driver driver){
        userService.setDeactive(driver);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Driver> addOrSave(@RequestBody Driver driver){
        return new ResponseEntity<Driver>(userService.addOrSave(driver),HttpStatus.CREATED);
    }

    @PostMapping("/login") // form data olarak gelmeli
    public ResponseEntity login(String email, String password){
        userService.Login(email, password);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}






