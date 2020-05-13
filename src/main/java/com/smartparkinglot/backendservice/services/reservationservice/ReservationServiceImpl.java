package com.smartparkinglot.backendservice.services.reservationservice;

import com.smartparkinglot.backendservice.domain.Reservation;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repositories.ReservationRepository;
import com.smartparkinglot.backendservice.services.driverservice.DriverService;
import com.smartparkinglot.backendservice.services.parkinglotservice.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service

public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    DriverService driverService;
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
        // otoparkın varlığını kontrol etmelimiyim, otopark yoksa zaten boş liste döndürüyor ??
        if (!driverService.isExistsById(reservation.getDriverId())) throw new NotFoundException("There isn't any driver registered with id:"+reservation.getDriverId());

        Reservation activeRes = reservationRepository.findByDriverIdAndIsActiveTrue(reservation.getDriverId());
        if (activeRes != null) throw new AlreadyExistsException("Already have an active reservation with this driver!");

        reservation.setActive(true);
        reservation.setHourlyWage(parkingLotService.getById(reservation.getParkingLotId()).getHourlyWage() / 3);
        return reservationRepository.save(reservation);

    }

    @Override
    public Reservation updateReservation(Reservation reservation) {

        // aktif bi rezervasyon cagrılmıs otoparka giriş yapılıyordur
        LocalDateTime createdAt = reservation.getCreatedAt();
        LocalDateTime deactivatedAt = LocalDateTime.now();
        reservation.setDeactivatedAt(deactivatedAt);
        Duration duration = Duration.between(createdAt,deactivatedAt);
        Long hoursPast = duration.toHours(); // otoparka gelene kadar geçen zaman veya iptal edilene kadar geçen zaman
        reservation.setCost((hoursPast +1) * reservation.getHourlyWage() ); // ücret otopark saatlik ücretinin 3te1 i olarak atanır
        reservation.setActive(false); // deaktif edilir
        return reservationRepository.save(reservation); // geri gönderilir

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

    @Override
    public List<Reservation> getDriverReservations(Long driver_id) {
        // kullanıcının varlığını kontrol etmelimiyim, otopark yoksa zaten boş liste döndürüyor ??
        if (!driverService.isExistsById(driver_id)) throw new NotFoundException("There isn't any driver registered with id:"+driver_id);
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
    public List<Reservation> getParkingLotReservations(Long parkingLotId) {
        // future work -> gelen parkinglotid ile parkinglot bulunup owner_id'si bulunur ve isteği yapan otoparkın id'si ile
        // karşılaştırılır böylece kullanıcının sadece kendi otoparkına erişimi bulunur


        if (!parkingLotService.isExistsById(parkingLotId)) throw new NotFoundException("There isn't any parking lot registered with id:"+parkingLotId);
        return reservationRepository.findByParkingLotId(parkingLotId);
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
    public Reservation findByParkingLotIdAndPlateAndIsActiveTrue(Long parkingLotId, String plate) {
        return reservationRepository.findByParkingLotIdAndPlateAndIsActiveTrue(parkingLotId,plate);
    }
}
