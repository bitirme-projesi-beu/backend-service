package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repositories.DriverRepository;
import com.smartparkinglot.backendservice.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;
    DriverRepository driverRepository;


    @Override
    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservation(Long id) {
            return reservationRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("There is no reservation under the given reservation id:"+id));

    }

    @Override
    public Reservation getReservation(String plate) {
        Reservation res = reservationRepository.findByPlate(plate);
        if (res == null ) throw new NotFoundException("There is no reservation under the given plate:"+plate);
        else return res;
    }

    @Override
    public Reservation addOrSave(Reservation reservation) {

        if (checkActiveReservation(reservation.getOwner().getId())) {
            throw new AlreadyExistsException("Already have an active reservation!");
        }
            reservation.setIsDeleted(false);
            reservation.setActive(true);
            return reservationRepository.save(reservation);

    }
    @Override
    public void delete(Reservation reservation) {
        if (reservation == null ){
            throw new NullPointerException();
        }else if (reservationRepository.findById(reservation.getId()) == null){
            throw new NotFoundException("Not found with id:"+reservation.getId());
        }
        else{
            reservationRepository.delete(reservation);
        }

        if (reservationRepository.findById(reservation.getId().longValue()).isPresent()){
            throw new AlreadyExistsException("Couldn't Delete");
        }
    }

    @Override
    public Boolean checkActiveReservation(Long owner_id) {
        List<Reservation> reservations = driverRepository.findById(owner_id).get().getReservationList();
        for (Reservation reservation: reservations){
            if (reservation.isActive()) return true;
        }
        return false;
    }
}
