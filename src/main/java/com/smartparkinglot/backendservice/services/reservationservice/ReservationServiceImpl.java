package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repositories.DriverRepository;
import com.smartparkinglot.backendservice.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
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
    public List<Reservation> getDriverReservations(Long driver_id) {
        List<Reservation> res = reservationRepository.findByDriverId(driver_id);
        if (res == null) throw new NotFoundException("No reservation under given driver id:"+driver_id);
        return res;
    }

    @Override
    public Reservation getReservation(Long driver_id,Long res_id) {
            Reservation reservation = reservationRepository.findById(res_id).orElseThrow(
                    () -> new NotFoundException("There is no reservation under the given reservation id:"+res_id));
            if (reservation.getDriverId() != driver_id) throw new NotFoundException("There is no reservation under this driver with this reservation id:"+res_id);
            return reservation;
    }

    @Override
    public List<Reservation> getReservationWithPlate(String plate) {
        List<Reservation> res = reservationRepository.findByPlate(plate);
        if (res == null ) throw new NotFoundException("There is no reservation under the given plate:"+plate);
        else return res;
    }

    @Override
    public Reservation addOrSave(Reservation reservation) {
        Reservation activeRes = reservationRepository.findByDriverIdAndIsActiveTrue(reservation.getDriverId());
        if (activeRes != null){
            throw new AlreadyExistsException("Already have an active reservation with this driver!");
        }
            reservation.setActive(true);
            return reservationRepository.save(reservation);

    }
    @Override
    public void deleteById(Reservation reservation) {
        if (reservation == null){
            throw new NullPointerException();
        }else{
            Long id =reservation.getId();
            reservationRepository.deleteById(id);
        }
    }

}
