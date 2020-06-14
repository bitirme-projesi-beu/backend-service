package com.smartparkinglot.backendservice.service.reservationservice;

import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.ReservationRepository;
import com.smartparkinglot.backendservice.service.accountservice.AccountService;
import com.smartparkinglot.backendservice.service.parkinglotservice.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service

public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ParkingLotService parkingLotService;


    @Override
    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationWithId(Long res_id) {
        Reservation reservation = reservationRepository.findById(res_id).orElseThrow(
                () -> new NotFoundException("There is no reservation under the given reservation id:"+res_id));
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)authentication.getPrincipal();

        // otoparkın varlığını kontrol etmelimiyim, otopark yoksa zaten boş liste döndürüyor ??
        if (!accountService.isExistsById(account.getId().longValue())) throw new NotFoundException("There isn't any driver registered with id:"+reservation.getDriverId());

        Reservation activeRes = reservationRepository.findByDriverIdAndIsActiveTrue(account.getId().longValue());
        if (activeRes != null) throw new AlreadyExistsException("Already have an active reservation with this driver!");
        if (parkingLotService.getById(reservation.getParkingLotId()).getActiveCapacity() <= 0) throw new NotFoundException("Parking lot is full");

        ParkingLot parkingLot = parkingLotService.getById(reservation.getParkingLotId());
        parkingLotService.updateParkingLotActiveCapacity(parkingLot,-1);

        reservation.setDriverId(account.getId().longValue());
        reservation.setActive(true);
        reservation.setHourlyWage(parkingLotService.getById(reservation.getParkingLotId()).getHourlyWage() / 3);
        reservation.setParkingLotName(parkingLotService.getById(reservation.getParkingLotId()).getName());
        return reservationRepository.save(reservation);

    }

    @Override
    public Reservation updateReservation(Reservation reservation,LocalDateTime now) {

        // aktif bi rezervasyon cagrılmıs otoparka giriş yapılıyordur
        LocalDateTime createdAt = reservation.getCreatedAt();
        LocalDateTime deactivatedAt = now;
        reservation.setDeactivatedAt(deactivatedAt);
        Duration duration = Duration.between(createdAt,deactivatedAt);
        Long hoursPast = duration.toHours(); // otoparka gelene kadar geçen zaman veya iptal edilene kadar geçen zaman
        reservation.setCost((hoursPast +1) * reservation.getHourlyWage() ); // ücret otopark saatlik ücretinin 3te1 i olarak atanır
        reservation.setActive(false); // deaktif edilir

        ParkingLot parkingLot = parkingLotService.getById(reservation.getParkingLotId());
        parkingLotService.updateParkingLotActiveCapacity(parkingLot,1);

        return reservationRepository.save(reservation); // geri gönderilir

    }

    @Override
    public void cancelReservation(Reservation reservation) {
        Reservation foundRes = reservationRepository.findById(reservation.getId()).orElseThrow( () -> new NotFoundException("no res with id:"+reservation.getId()));
        ParkingLot parkingLot = parkingLotService.getById(reservation.getParkingLotId());
        parkingLotService.updateParkingLotActiveCapacity(parkingLot,1);
        foundRes.setDeactivatedAt(reservation.getCreatedAt());
        Duration duration = Duration.between(foundRes.getCreatedAt(),reservation.getCreatedAt());
        Long hoursPast = duration.toHours(); // otoparka gelene kadar geçen zaman veya iptal edilene kadar geçen zaman
        foundRes.setCost((hoursPast +1) * foundRes.getHourlyWage() ); // ücret otopark saatlik ücretinin 3te1 i olarak atanır
        foundRes.setActive(false); // deaktif edilir

        reservationRepository.save(foundRes);
    }

    @Override
    public void deleteById(Reservation reservation) {
        if (reservation == null){
            throw new NullPointerException();
        }else{
            ParkingLot parkingLot = parkingLotService.getById(reservation.getParkingLotId());
            parkingLotService.updateParkingLotActiveCapacity(parkingLot,1);

            Long id =reservation.getId();
            reservationRepository.deleteById(id);
        }
    }

    @Override
    public List<Reservation> getDriverReservations(Long driver_id) {
        // kullanıcının varlığını kontrol etmelimiyim, otopark yoksa zaten boş liste döndürüyor ??
        if (!accountService.isExistsById(driver_id)) throw new NotFoundException("There isn't any driver registered with id:"+driver_id);
        List<Reservation> res = reservationRepository.findByDriverId(driver_id);

        if (res == null) throw new NotFoundException("No reservation under given driver id:"+driver_id);
        return res;
    }
    @Override
    public Reservation getDriverReservation(Long driver_id,Long res_id) {
            Reservation reservation = reservationRepository.findById(res_id).orElseThrow(
                    () -> new NotFoundException("There is no reservation under the given reservation id:"+res_id));
            if (reservation.getDriverId() != driver_id) throw new NotFoundException("There is no reservation under this driver with this reservation id:"+res_id);
            return reservation;
    }

    @Override
    public List<Reservation> getDriverReservationsForDriver() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)authentication.getPrincipal();

        List<Reservation> reservations = reservationRepository.findByDriverIdByOrderByCreatedAtDesc(account.getId());
        if (reservations == null) throw new NotFoundException("no reservations to show with that id:"+account.getId().toString());
        return  reservations;
    }

    @Override
    public Reservation getDriverReservationForDriver(Long res_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)authentication.getPrincipal();

        Reservation reservation = reservationRepository.findById(res_id).orElseThrow(() -> new NotFoundException("not found with that res id:"+res_id));
        if (account.getId() != reservation.getDriverId()) throw new NotFoundException("this res has no connection with this driver id:"+account.getId());
        return reservation;
    }

    @Override
    public Reservation getParkingLotReservation(Long parkingLotId, Long reservationId) {
        // otoparkın varlığını kontrol etmelimiyim, otopark yoksa zaten boş liste döndürüyor ??
        if (!parkingLotService.isExistsById(parkingLotId)) throw new NotFoundException("There isn't any parking lot registered with id:"+parkingLotId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NotFoundException("There is no reservation with id"+reservationId));
        if (reservation.getParkingLotId() != parkingLotId ) throw new NotFoundException("There is no reservation under this parking lot with reservation id:"+reservationId);
        return reservation;
    }

    @Override
    public List<Reservation> getParkingLotReservations(Long parkingLotId) {
        if (!parkingLotService.isExistsById(parkingLotId)) throw new NotFoundException("There isn't any parking lot registered with id:"+parkingLotId);
        List<Reservation> reservations = reservationRepository.findByParkingLotId(parkingLotId);
        if (reservations == null ) throw new NotFoundException("There is no reservation under this parking lot id:"+parkingLotId);
        return reservations;
    }


    @Override
    public Reservation findByParkingLotIdAndPlateAndIsActiveTrue(Long parkingLotId, String plate) {
        return reservationRepository.findByParkingLotIdAndPlateAndIsActiveTrue(parkingLotId,plate);
    }

}