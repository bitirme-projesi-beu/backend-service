package com.smartparkinglot.backendservice.controller;


import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.service.parkinglotservice.ParkingLotService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/parkinglots")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ParkingLotController {
    @Autowired
    ParkingLotService parkingLotService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ParkingLot>> getAllParkingLots(){
        return new ResponseEntity<List<ParkingLot>>(parkingLotService.getAllParkingLots(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<ParkingLot> getById(@PathVariable Long id){
        return new ResponseEntity<ParkingLot>(parkingLotService.getById(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ParkingLot> addOrSave(@RequestBody ParkingLot parkingLot){
        return new ResponseEntity<ParkingLot>(parkingLotService.addOrSave(parkingLot),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER') || hasRole('ROLE_ADMIN')")
    @DeleteMapping()
    @Operation(summary = "deactivates parking lot for end-user")
    public ResponseEntity deactiveParkingLot(@RequestBody ParkingLot parkingLot){
        parkingLotService.setDeactive(parkingLot);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("admin-delete")
    @Operation(summary = "deletes parking lot for admin")
    public ResponseEntity deleteParkingLot(@RequestBody ParkingLot parkingLot){
        parkingLotService.deleteParkingLot(parkingLot);
        return new ResponseEntity(HttpStatus.OK);
    }
}






