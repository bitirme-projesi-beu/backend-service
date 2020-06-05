package com.smartparkinglot.backendservice.controller;

import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.service.parkinglotservice.ParkingLotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/parkinglots")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_DRIVER')")
    @GetMapping @Operation(summary = "Retrieves all parking lots for ADMIN", security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<List<ParkingLot>> getAllParkingLots(){
        return new ResponseEntity<List<ParkingLot>>(parkingLotService.getAllParkingLots(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PL.OWNER')")
    @GetMapping("my-parking-lots") @Operation(summary = "it shows authenticated PL Owner's parking lots for PL OWNERS",security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<List<ParkingLot>> getPLOwnersParkingLots(){
        return new ResponseEntity<List<ParkingLot>>(parkingLotService.findPLOwnersParkingLots(),HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("{id}")@Operation(summary = "Get parking lot via P.L ID for ADMIN", security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<ParkingLot> getById(@PathVariable Long id){
        return new ResponseEntity<ParkingLot>(parkingLotService.getById(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<ParkingLot> addOrSave(@RequestBody ParkingLot parkingLot){
        return new ResponseEntity<ParkingLot>(parkingLotService.addOrSave(parkingLot),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER') || hasRole('ROLE_ADMIN')")
    @DeleteMapping()
    @Operation(summary = "deactivates parking lot for end-user",security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity deactiveParkingLot(@RequestBody ParkingLot parkingLot){
        parkingLotService.setDeactive(parkingLot);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("admin-delete")
    @Operation(summary = "deletes parking lot for admin",security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity deleteParkingLot(@RequestBody ParkingLot parkingLot){
        parkingLotService.deleteParkingLot(parkingLot);
        return new ResponseEntity(HttpStatus.OK);
    }
}
