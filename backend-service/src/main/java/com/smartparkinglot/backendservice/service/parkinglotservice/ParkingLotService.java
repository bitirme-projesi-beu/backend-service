package com.smartparkinglot.backendservice.service.parkinglotservice;

import com.smartparkinglot.backendservice.domain.ParkingLot;

import java.util.List;

public interface ParkingLotService {
    List<ParkingLot> getAllParkingLots();
    ParkingLot getById(Long id);
    ParkingLot addOrSave(ParkingLot parkingLot);
    void deleteParkingLot(ParkingLot parkingLot);
    void setDeactive(ParkingLot parkingLot);
    Boolean isExistsById(Long parkinglotId);

    void updateRating(Long id);
    void updateParkingLotActiveCapacity(ParkingLot parkingLot,Integer addition);
}
