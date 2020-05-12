package com.smartparkinglot.backendservice.services.driverservice;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.domain.Reservation;

import java.util.List;

public interface DriverService {
    List<Driver> getAllDrivers();
    Driver getById(Long id);
    Driver addOrSave(Driver driver);
    void deleteDriver(Driver driver);
    void setDeactive(Driver driver);
    Driver Login(String email, String password);
}
