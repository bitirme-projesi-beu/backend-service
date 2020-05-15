package com.smartparkinglot.backendservice.controller;

import com.smartparkinglot.backendservice.domain.Ticket;
import com.smartparkinglot.backendservice.service.ticketservice.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tickets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(){
        return new ResponseEntity<List<Ticket>>(ticketService.getAllTickets(),HttpStatus.FOUND);
    }

    @GetMapping("{ticketId}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long ticketId){
        return new ResponseEntity<Ticket>(ticketService.getTicket(ticketId),HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<Ticket> addOrSave(@RequestBody Ticket ticket){
        return new ResponseEntity<Ticket>(ticketService.addOrSave(ticket),HttpStatus.CREATED);
    }
    @DeleteMapping
    public ResponseEntity deleteTicket(@RequestBody Ticket ticket){
        ticketService.deleteTicket(ticket);
        return new ResponseEntity( HttpStatus.OK);
    }

    @GetMapping("drivers/{driver_id}/{ticketId}")
    public ResponseEntity<Ticket> getDriverTicket(@PathVariable Long driver_id,@PathVariable Long ticketId){
        return new ResponseEntity<Ticket>(ticketService.getDriverTicket(driver_id,ticketId),HttpStatus.FOUND);
    }

    @GetMapping("drivers/{driver_id}")
    public ResponseEntity<List<Ticket>> getDriverReservations(@PathVariable Long driver_id){
        return new ResponseEntity<List<Ticket>>(ticketService.getDriverTickets(driver_id),HttpStatus.FOUND);
    }

    @GetMapping("parkinglots/{parkingLotId}/{ticketId}")
    public ResponseEntity<Ticket> getParkingLotTicket(@PathVariable Long parkingLotId,@PathVariable Long ticketId){
        return new ResponseEntity<Ticket>(ticketService.getParkingLotTicket(parkingLotId,ticketId),HttpStatus.FOUND);
    }

    @GetMapping("parkinglots/{parkingLotId}")
    public ResponseEntity<List<Ticket>> getParkingLotTickets(@PathVariable Long parkingLotId){
        return new ResponseEntity<List<Ticket>>(ticketService.getParkingLotTickets(parkingLotId),HttpStatus.FOUND);
    }
}
