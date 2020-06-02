package com.smartparkinglot.backendservice.service.parkinglotservice;

import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.domain.Ticket;
import com.smartparkinglot.backendservice.exceptions.AccountDeactivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.ParkingLotRepository;
import com.smartparkinglot.backendservice.service.ticketservice.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository;
    @Autowired
    TicketService ticketService;

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
        String lotName = parkingLot.getName().toString();

        Long parkingLotId = parkingLot.getId();
        String parkingLotName = parkingLot.getName().toString();

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
    public void updateRating(Long id){
        Double total = 0.0;
        for (Ticket ticket: ticketService.getParkingLotTickets(id)){
            total += ticket.getRating();
        }
        total /= 5.0;
        total = (double)(Math.round(total*100)/100);
        ParkingLot parkingLot = parkingLotRepository.findById(id).get();
        parkingLot.setRating(total);
        parkingLotRepository.save(parkingLot);
    }
}
