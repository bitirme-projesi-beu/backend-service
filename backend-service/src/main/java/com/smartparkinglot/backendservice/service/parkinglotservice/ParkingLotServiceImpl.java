package com.smartparkinglot.backendservice.service.parkinglotservice;

import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.domain.Ticket;
import com.smartparkinglot.backendservice.exceptions.AccountDeactivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.ParkingLotRepository;
import com.smartparkinglot.backendservice.service.accountservice.AccountService;
import com.smartparkinglot.backendservice.service.ticketservice.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    AccountService accountService;

    @Override
    public List<ParkingLot> getAllParkingLots() {
        List<ParkingLot> parkingLots = parkingLotRepository.findAll();
        if (parkingLots == null){
            throw new NullPointerException();
        }
        return parkingLots;
    }

    @Override
    public ParkingLot getById(Long id) {
        ParkingLot parkingLot = parkingLotRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Parking Lot not found with id: "+id));
        return parkingLot;

    }

    @Override
    public ParkingLot addOrSave(ParkingLot parkingLot) {

        String parkingLotName = parkingLot.getName();
        Long ownerId = parkingLot.getParkingLotOwnerId();
        if(!accountService.getById(ownerId).getAuthorities()
                .stream().anyMatch(
                        a -> ((GrantedAuthority) a).getAuthority().equals("ROLE_PL.OWNER"))
        ) throw new NotFoundException("Given owner account is not PL OWNER ACCOUNT");
        if (parkingLot.getLatitude() == null || parkingLot.getLongtitude() == null) throw new NotFoundException("koordinatlar bos olamaz!");
        if (parkingLot == null && parkingLotRepository.findByName(parkingLotName) != null ){
            ParkingLot found_parking_lot = parkingLotRepository.findByName(parkingLotName);
            if (found_parking_lot.getIsDeleted()){
                /*
                found_parking_lot.setIsDeleted(false);
                parkingLotRepository.save(found_parking_lot);
                */
                throw new AccountDeactivatedException("Parkinglot is deleted, reach to admin to reactivate it again.");
            }
            throw new AlreadyExistsException("Parking lot already exists with given credentials");
        }else{
            parkingLot.setIsDeleted(false);
            parkingLot.setActiveCapacity(parkingLot.getMaxCapacity());
            return parkingLotRepository.save(parkingLot);
        }
    }

    @Override
    public void deleteParkingLot(ParkingLot parkingLot) {
        if (parkingLot.getId() == null)
            throw new NotFoundException("There is no id attached to parking lot");
        try{
            parkingLotRepository.deleteById(parkingLot.getId());
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void setDeactive(ParkingLot parkingLot) {
        ParkingLot parkingLot1 = parkingLotRepository.findById(parkingLot.getId()).get();
        parkingLot1.setIsDeleted(true);
        parkingLotRepository.save(parkingLot1);
    }

    @Override
    public Boolean isExistsById(Long parkinglotId) {
        return parkingLotRepository.existsById(parkinglotId);
    }

    @Override
    public void updateParkingLotActiveCapacity(ParkingLot parkingLot, Integer addition) {
        parkingLot.setActiveCapacity(parkingLot.getActiveCapacity()+ (addition));
        parkingLotRepository.save(parkingLot);
    }

    @Override
    public List<ParkingLot> findPLOwnersParkingLots() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)auth.getPrincipal();
        return parkingLotRepository.findByParkingLotOwnerId(account.getId().longValue());
    }

    @Override
    public void updateRating(Long parkingLotId){
        Double totalPoint = 0.0;
        Integer ticketsSize = 0;
        for (Ticket ticket: ticketService.getParkingLotTickets(parkingLotId)){
            totalPoint += ticket.getRating();
            ticketsSize++;
        }
        Double avg = totalPoint / ticketsSize;
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId).get();
        parkingLot.setRating(avg);
        parkingLotRepository.save(parkingLot);
    }
}