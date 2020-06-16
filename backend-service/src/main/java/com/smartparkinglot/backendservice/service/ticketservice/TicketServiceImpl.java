package com.smartparkinglot.backendservice.service.ticketservice;

import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.domain.Ticket;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.TicketRepository;
import com.smartparkinglot.backendservice.service.accountservice.AccountService;
import com.smartparkinglot.backendservice.service.parkinglotservice.ParkingLotService;
import com.smartparkinglot.backendservice.service.reservationservice.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    ParkingLotService parkingLotService;
    @Autowired
    AccountService accountService;
    @Autowired
    ReservationService reservationService;

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }


    @Override
    public List<Ticket> getParkingLotTickets(Long parkingLotId) {
        if (!parkingLotService.isExistsById(parkingLotId)) throw new NotFoundException("There isn't any parking lot registered with id:"+parkingLotId);
        return ticketRepository.findByParkingLotId(parkingLotId);
    }

    @Override
    public Ticket getParkingLotTicket(Long parkingLotId, Long ticketId) {
        // otoparkın varlığını kontrol etmelimiyim, otopark yoksa zaten boş liste döndürüyor ??
        if (!parkingLotService.isExistsById(parkingLotId)) throw new NotFoundException("There isn't any parking lot registered with id:"+parkingLotId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new NotFoundException("There is no ticket with id"+ticketId));
        if (ticket.getParkingLotId() != parkingLotId ) throw new NotFoundException("There is no ticket under this parking lot with ticket id:"+ticketId);
        return ticket;
    }


    @Override
    public List<Ticket> getDriverTickets(Long driverId) {
        // future work -> driver ile admin erişebilmeli
            // gelen driverid ile authenticate olan kişinin id'si uyuşuyor ise isteği yerine getirlir.
        if (!accountService.isExistsById(driverId)) throw new NotFoundException("There isn't any driver registered with id:"+driverId);
        List<Ticket> ticketList = ticketRepository.findByDriverId(driverId);
        return ticketList;

    }


    @Override
    public List<Ticket> getDriverTicketsForDriver() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)authentication.getPrincipal();
        List<Ticket> ticketList = ticketRepository.findByDriverIdOrderByCreatedAtDesc(account.getId().longValue());
        if (ticketList == null) throw new NotFoundException("no ticket list found with this driver id:"+account.getId().toString());
        return ticketList;
    }

    @Override
    public Ticket getDriverTicketForDriver(Long ticketId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)authentication.getPrincipal();

        Ticket ticket = ticketRepository.findById(ticketId).get();
        if (ticket.getDriverId() != account.getId()) throw new NotFoundException("this ticket not under record for this driver id:"+account.getId());
        return  ticket;
    }

    @Override
    public Ticket getDriverTicket(Long driverId, Long ticketId) {
        if (!accountService.isExistsById(driverId)) throw new NotFoundException("There isn't any driver registered with id:"+driverId);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new NotFoundException("There isn't any ticket with id:"+ticketId) );
        if (ticket.getDriverId() != driverId) throw new NotFoundException("There isn't any ticket under requested driver with this ticket id"+ticketId);
        return ticket;
    }

    @Override
    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(
                () -> new NotFoundException("There is no ticket under given id:"+ticketId)
        );
    }

    @Override
    public Ticket addOrSave(Ticket ticket) {
        Long parkingLotId = ticket.getParkingLotId();
        if (parkingLotId == null) throw new NotFoundException("There must be a parking lot id attachted to ticket");
        if (!parkingLotService.isExistsById(parkingLotId)) throw new NotFoundException("There must be a valid parking lot id attachted to ticket");

        String plate = ticket.getPlate();
        if (plate.trim().equals("") || plate == null) throw new NotFoundException("There must be a plate attachted to ticket");
        Double hourlyWage = parkingLotService.getById(parkingLotId).getHourlyWage();

        Reservation activeReservation = reservationService.findByParkingLotIdAndPlateAndIsActiveTrue(parkingLotId,plate);
        Ticket someoneExiting = ticketRepository.findByParkingLotIdAndPlateAndIsItInsideTrue(parkingLotId,plate);

        if (activeReservation != null){
            activeReservation = reservationService.updateReservation(activeReservation,ticket.getCreatedAt()); // end the reservation and prepare to start a ticket
            Ticket startingTicket = startTheTicket(ticket,activeReservation.getDriverId(),activeReservation.getCost(),true);
            startingTicket.setParkingLotName(parkingLotService.getById(startingTicket.getParkingLotId()).getName());
            return ticketRepository.save(startingTicket);
        }
        else if(someoneExiting != null) {
            Ticket exitingTicket = endTheTicket(someoneExiting, hourlyWage, ticket.getCreatedAt());
            return ticketRepository.save(exitingTicket);
        }
        else {
            // if there is no active res with that plate in that parking lot and if there is no exit case it means guest entering parking lot
            Ticket startingGuestTicket = startTheTicket(ticket,Long.valueOf(-1),0.0,true);
            startingGuestTicket.setParkingLotName(parkingLotService.getById(startingGuestTicket.getParkingLotId()).getName());
            return ticketRepository.save(startingGuestTicket);
        }
    }
    @Override
    public void deleteTicket(Ticket ticket) {
        if (ticket == null){
            throw new NotFoundException("Ticket is empty");
        }else{
            ParkingLot parkingLot = parkingLotService.getById(ticket.getParkingLotId());
            parkingLotService.updateParkingLotActiveCapacity(parkingLot,1);

            Long id = ticket.getId();
            ticketRepository.deleteById(id);
        }
    }

    @Override
    public Ticket rateTicket(Ticket ticket) {
        Ticket ticket1 = ticketRepository.findById(ticket.getId()).get();
        ticket1.setRating(ticket.getRating());
        Ticket ticket2 = ticketRepository.save(ticket1);
        if (ticket2 != null) parkingLotService.updateRating(ticket1.getParkingLotId());
       return ticket1;
    }


    private Ticket startTheTicket(Ticket ticket,Long driverId, Double cost, boolean isItInside) {

        ParkingLot parkingLot = parkingLotService.getById(ticket.getParkingLotId());
        if (parkingLot.getActiveCapacity() == 0) throw new NotFoundException("Parkinglot is full!");
        parkingLotService.updateParkingLotActiveCapacity(parkingLot,-1);

        ticket.setDriverId(driverId);
        ticket.setCost(cost);
        ticket.setIsItInside(isItInside);
        return ticket;
    }

    private Ticket endTheTicket(Ticket isSomeOneExiting, Double hourlyWage, LocalDateTime now) {
        LocalDateTime enterTime = isSomeOneExiting.getCreatedAt();
        LocalDateTime exitTime = now;
        Long hoursPast = Duration.between(enterTime,exitTime).toHours();
        isSomeOneExiting.setExitedAt(exitTime);
        isSomeOneExiting.addToTheCost(hourlyWage * (hoursPast+1));
        isSomeOneExiting.setIsItInside(false);

        ParkingLot parkingLot = parkingLotService.getById(isSomeOneExiting.getParkingLotId());
        parkingLotService.updateParkingLotActiveCapacity(parkingLot,1);


        return isSomeOneExiting;
    }
}