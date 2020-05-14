package com.smartparkinglot.backendservice.services.ticketservice;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.domain.Ticket;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repositories.TicketRepository;
import com.smartparkinglot.backendservice.services.driverservice.DriverService;
import com.smartparkinglot.backendservice.services.parkinglotservice.ParkingLotService;
import com.smartparkinglot.backendservice.services.reservationservice.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    DriverService driverService;
    @Autowired
    ReservationService reservationService;

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getParkingLotTickets(Long parkingLotId) {
        // future work -> gelen parkinglotid ile parkinglot bulunup owner_id'si bulunur ve isteği yapan kişinin id'si ile
            // karşılaştırılır böylece kullanıcının sadece kendi otoparkına erişimi bulunur

        // otoparkın varlığını kontrol etmelimiyim, otopark yoksa zaten boş liste döndürüyor ??
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
        if (!driverService.isExistsById(driverId)) throw new NotFoundException("There isn't any driver registered with id:"+driverId);
        List<Ticket> ticketList = ticketRepository.findByDriverId(driverId);
        return ticketList;

    }

    @Override
    public Ticket getDriverTicket(Long driverId, Long ticketId) {
        if (!driverService.isExistsById(driverId)) throw new NotFoundException("There isn't any driver registered with id:"+driverId);
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
        if ( !parkingLotService.isExistsById(parkingLotId) ) throw new NotFoundException("There must be a valid parking lot id attachted to ticket");
        String plate = ticket.getPlate();
        if (plate.trim().equals("") || plate == null) throw new NotFoundException("There must be a plate attachted to ticket");

        //reservation check
        Reservation reservation = reservationService.findByParkingLotIdAndPlateAndIsActiveTrue(parkingLotId,plate);
        Double hourlyWage = parkingLotService.getById(parkingLotId).getHourlyWage();
        Ticket isSomeOneExiting = ticketRepository.findByParkingLotIdAndPlateAndIsItInsideTrue(parkingLotId,plate);
        // ticket dan gelen plaka ve otopark id ile içerde isitinside aktif varsa misafir çıkıyor demek
        if (reservation != null){
            // we have a reservation and its still active so that means driver is entering parking lot
            reservation = reservationService.updateReservation(reservation);
            ticket.setDriverId(reservation.getDriverId());
            ticket.setCost(reservation.getCost());
            ticket.setIsItInside(true);
            return ticketRepository.save(ticket);
        }else if(isSomeOneExiting != null) {
            LocalDateTime enterTime = isSomeOneExiting.getCreatedAt();
            LocalDateTime exitTime = ticket.getCreatedAt();             // burda degıl rezervasyon zamanlarında sıkıntı olabilir
            Long hoursPast = Duration.between(enterTime,exitTime).toHours();
            isSomeOneExiting.setExitedAt(exitTime);
            isSomeOneExiting.addToTheCost(hourlyWage * (hoursPast+1));
            isSomeOneExiting.setIsItInside(false);
            return ticketRepository.save(isSomeOneExiting);
        } //if(ticket.getIsItInside() == null)
        else {
            // if there is no active res with that plate in that parking lot and if there is no exit case it means guest entering parking lot
            ticket.setDriverId(Long.valueOf(-1));
            ticket.setIsItInside(true);
            ticket.setCost(0.0);
            return ticketRepository.save(ticket);
            // manual payment.
        }
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        if (ticket == null){
            throw new NotFoundException("Ticket is empty");
        }else{
            Long id = ticket.getId();
            ticketRepository.deleteById(id);
        }
    }
}
