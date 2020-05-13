package com.smartparkinglot.backendservice.services.parkinglotservice;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.domain.ParkingLot;

import java.util.List;

public interface ParkingLotService {
    List<ParkingLot> getAllParkingLots();
    ParkingLot getById(Long id);
    ParkingLot addOrSave(ParkingLot parkingLot);
    void deleteParkingLot(ParkingLot parkingLot);
    void setDeactive(ParkingLot parkingLot);
}
