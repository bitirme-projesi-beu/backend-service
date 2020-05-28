package com.smartparkinglot.backendservice.service.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    /*
        tüm rezervasyonları listeler
     */
    List<Reservation> listAll();

    /*
        rez. id ile rezervasyon arama
     */
    Reservation getReservationWithId(Long res_id); // adminlere acik

    /*
        plaka ile rezervasyon arama
     */
    List<Reservation> getReservationWithPlate(String plate);

    /*
        adds new reservation
     */
    Reservation addOrSave(Reservation reservation);

    /*
        deletes given reservation object
     */
    void deleteById(Reservation reservation); // admin  privilege

    /*
        in case of user cancels her reservation or enters parking lot this function
     */
    Reservation updateReservation(Reservation reservation, LocalDateTime now);

    /*
        returns driver reservations
     */
    List<Reservation> getDriverReservations(Long driver_id);

    /*
        return reservation with given id under the given driver if that res. belong that driver
     */
    Reservation getDriverReservation(Long driver_id, Long res_id); // user'a acık

    /*
        returns parking lot's reservations
     */
    List<Reservation> getParkingLotReservations(Long parkingLotId);

    /*
       return reservation with given id under the given parkinglot if that res. belong that parkinglot
    */
    Reservation getParkingLotReservation(Long parkingLotId, Long reservationId);


    /*
        return reservation that has following conditions
            -> given plate and parking lot id and and active status is true must match
     */
    Reservation findByParkingLotIdAndPlateAndIsActiveTrue(Long parkingLotId, String plate);



}
