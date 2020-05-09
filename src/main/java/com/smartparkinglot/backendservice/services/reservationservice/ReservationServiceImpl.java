package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;
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
    public ResponseEntity<List<Reservation>> listAll() {
        return new ResponseEntity<List<Reservation>>(reservationRepository.findAll(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Reservation>> getDriverReservations(Long owner_id) {
        try{
            List<Reservation> all_reservations = reservationRepository.findAll();

            for (Reservation reservation:  all_reservations  ){
                if (reservation.getOwnerId() != owner_id ){
                    all_reservations.remove(reservation);
                }
            }
            return new ResponseEntity<List<Reservation>>(all_reservations,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<List<Reservation>>(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<Reservation> getReservation(String plate) {
        try{
            return new ResponseEntity<Reservation>(reservationRepository.findByPlate(plate),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Reservation>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Reservation> addOrSave(Reservation reservation) {

        if (checkActiveReservation()){
            return new ResponseEntity<Reservation>(HttpStatus.CONFLICT); // there is currently active reservation
        }

        try{
            reservation.setActive(true);
            return new ResponseEntity<Reservation>(reservationRepository.save(reservation),HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Reservation>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity deleteById(Long reservation_id) {
        try {
            reservationRepository.deleteById(reservation_id);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
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
