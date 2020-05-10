package com.smartparkinglot.backendservice.controllers;


import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.services.driverservice.DriverService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class DriverController {
    private DriverService userService;
    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers(){
        return new ResponseEntity<List<Driver>>(userService.getAllDrivers(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getById(@PathVariable Long id){
        return new ResponseEntity<Driver>(userService.getById(id),HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteDriver(@RequestBody Driver driver){
        userService.deleteDriver(driver);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Driver> addOrSave(@RequestBody Driver driver){
        return new ResponseEntity<Driver>(userService.addOrSave(driver),HttpStatus.CREATED);
    }

    @GetMapping("/login") // form data olarak gelmeli
    public ResponseEntity login(String email, String password){
        userService.Login(email, password);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}






