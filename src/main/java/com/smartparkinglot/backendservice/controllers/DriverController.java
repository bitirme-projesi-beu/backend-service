package com.smartparkinglot.backendservice.controllers;


import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.services.driverservice.DriverService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userService.getAllDrivers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getById(@PathVariable Long id){
        return userService.getById(id);
    }

    @DeleteMapping
    public ResponseEntity deleteDriver(Driver driver){
       return userService.deleteDriver(driver);
    }

    @PostMapping("/register")
    public ResponseEntity<Driver> addOrSave(@RequestBody Driver driver){
        return userService.addOrSave(driver);

    }

    @GetMapping("/login") // form data olarak gelmeli
    public ResponseEntity login(String email, String password){
        return userService.Login(email, password);
    }
}






