package com.smartparkinglot.backendservice.service.ticketservice;

import com.smartparkinglot.backendservice.domain.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getAllTickets();

    List<Ticket> getParkingLotTickets(Long parkingLotId);
    Ticket getParkingLotTicket(Long parkingLotId, Long ticketId);

    List<Ticket> getDriverTickets(Long driverId);
    List<Ticket> getDriverTicketsForDriver();

    Ticket getDriverTicket(Long driverId, Long ticketId);
    Ticket getDriverTicketForDriver(Long ticketId);


    Ticket getTicket(Long ticketId);
    Ticket addOrSave(Ticket ticket);
    void deleteTicket(Ticket ticket);

    Ticket rateTicket(Ticket ticket);
}
