package com.smartparkinglot.backendservice.services.driverservice;

import com.smartparkinglot.backendservice.domain.Driver;
import org.springframework.http.ResponseEntity;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public interface DriverService {
    ResponseEntity<List<Driver>> getAllDrivers();
    ResponseEntity<Driver> getById(Long id);
    ResponseEntity<Driver> addOrSave(Driver driver);
    ResponseEntity deleteDriver(Driver driver);
    ResponseEntity Login(String email, String password);
}
