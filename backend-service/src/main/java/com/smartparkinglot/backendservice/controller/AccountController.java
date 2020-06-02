package com.smartparkinglot.backendservice.controller;


import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.service.driverservice.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    @Autowired
    DriverService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllDrivers(){
        return new ResponseEntity<List<Account>>(accountService.getAllDrivers(),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id){
        return new ResponseEntity<Account>(accountService.getById(id),HttpStatus.OK);
    }

    @PostMapping("sign-up")
    public ResponseEntity register(@RequestBody Account account){
        accountService.addOrSave(account);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("authenticate")
    public ResponseEntity<String> login(@RequestBody Account account){
        return new ResponseEntity<String>(accountService.Login(account),HttpStatus.OK);
    }

    @DeleteMapping("delete")
    @Operation(summary = "deactivates user for end-user ")
    public ResponseEntity deactiveDriver(@RequestBody Account account){
        accountService.setDeactive(account);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("admin-delete")
    @Operation(summary = "deletes user for admin")
    public ResponseEntity deleteDriver(@RequestBody Account account){
        accountService.deleteDriver(account);
        return new ResponseEntity(HttpStatus.OK);
    }
}






