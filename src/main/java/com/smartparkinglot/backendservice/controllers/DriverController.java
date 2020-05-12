package com.smartparkinglot.backendservice.controllers;


import com.smartparkinglot.backendservice.domain.Driver;
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
    private DriverService driverService;


    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers(){
        return new ResponseEntity<List<Driver>>(driverService.getAllDrivers(),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Driver> getById(@PathVariable Long id){
        return new ResponseEntity<Driver>(driverService.getById(id),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Driver> addOrSave(@RequestBody Driver driver){
        return new ResponseEntity<Driver>(driverService.addOrSave(driver),HttpStatus.CREATED);
    }

    @PostMapping("/login") // form data olarak gelmeli
    public ResponseEntity login(String email, String password){
        driverService.Login(email, password);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
    @DeleteMapping()
    public ResponseEntity deactiveDriver(@RequestBody Driver driver){
        driverService.setDeactive(driver);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity deleteDriver(@RequestBody Driver driver){
        driverService.deleteDriver(driver);
        return new ResponseEntity(HttpStatus.OK);
    }
}






