package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.exceptions.reservationexceptions.ActiveReservationExistsException;
import com.smartparkinglot.backendservice.exceptions.reservationexceptions.ReservationNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.jdbc.Expectation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.smartparkinglot.backendservice.repositories.ReservationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;

    @Override
    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getDriverReservations(Long owner_id) {
        try{
            List<Reservation> all_reservations = reservationRepository.findAll();

            for (Reservation reservation:  all_reservations  ){
                if (reservation.getOwnerId() != owner_id ){
                    all_reservations.remove(reservation);
                }
            }
            return all_reservations;
        }catch (Exception e){
            throw new ReservationNotFoundException("There is no reservation");
        }

    }

    @Override
    public Reservation getReservation(String plate) {
        try{
            return reservationRepository.findByPlate(plate);
        }catch (Exception e){
            throw new ReservationNotFoundException("There is no reservation under the given plate:"+plate);
        }
    }

    @Override
    public Reservation addOrSave(Reservation reservation) {

        if (checkActiveReservation()) {
            throw new ActiveReservationExistsException("Already have an active reservation!");
        }

            reservation.setActive(true);
            return reservationRepository.save(reservation);

    }
    @Override
    public void deleteById(Long reservation_id) {
        if (reservation_id == null){
            throw new NullPointerException();
        }else{
            reservationRepository.deleteById(reservation_id);
        }
    }

    @Override
    public Boolean checkActiveReservation() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation: reservations){
            if (reservation.isActive()) return true;
        }
        return false;
    }
}
