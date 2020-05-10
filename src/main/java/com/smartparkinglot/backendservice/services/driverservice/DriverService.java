package com.smartparkinglot.backendservice.services.driverservice;

import com.smartparkinglot.backendservice.domain.Driver;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DriverService {
    List<Driver> getAllDrivers();
    Driver getById(Long id);
    Driver addOrSave(Driver driver);
    void deleteDriver(Driver driver);
    Boolean Login(String email, String password);
}
