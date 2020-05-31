package com.smartparkinglot.backendservice.controller;


import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.service.parkinglotservice.ParkingLotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/parkinglots")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ParkingLotController {
    @Autowired
    ParkingLotService parkingLotService;

    @GetMapping
    public ResponseEntity<List<ParkingLot>> getAllDrivers(){
        return new ResponseEntity<List<ParkingLot>>(parkingLotService.getAllParkingLots(),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ParkingLot> getById(@PathVariable Long id){
        return new ResponseEntity<ParkingLot>(parkingLotService.getById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParkingLot> addOrSave(@RequestBody ParkingLot parkingLot){
        return new ResponseEntity<ParkingLot>(parkingLotService.addOrSave(parkingLot),HttpStatus.CREATED);
    }


    @DeleteMapping()
    @Operation(summary = "USE it for deletion")
    public ResponseEntity deactiveParkingLot(@RequestBody ParkingLot parkingLot){
        parkingLotService.setDeactive(parkingLot);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("admin-delete")
    @Operation(summary = "DONT USE it for deletion")
    public ResponseEntity deleteParkingLot(@RequestBody ParkingLot parkingLot){
        parkingLotService.deleteParkingLot(parkingLot);
        return new ResponseEntity(HttpStatus.OK);
    }
}






