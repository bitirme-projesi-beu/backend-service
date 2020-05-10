package com.smartparkinglot.backendservice.services.driverservice;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.exceptions.driverexceptions.DriverAlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.driverexceptions.DriverNotFoundException;
import com.smartparkinglot.backendservice.exceptions.driverexceptions.WrongCredentialsException;
import com.smartparkinglot.backendservice.repositories.DriverRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.validation.constraints.Null;
import java.net.HttpURLConnection;
import java.util.List;


@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {
    DriverRepository driverRepository;

    @Override
    public List<Driver> getAllDrivers() {
        List<Driver> driverList = driverRepository.findAll();
        if (driverList == null){
            throw new NullPointerException();
        }
        return driverList;
    }

    @Override
    public Driver getById(Long id) {
        return driverRepository.findById(id).orElseThrow(
                () -> new DriverNotFoundException("Driver not found with id: "+id));
    }

    @Override
    public Driver addOrSave(Driver driver) {
        Long driver_id = driver.getId();
        String driver_email = driver.getEmail();

        if (driver_id == null && driverRepository.findByEmail(driver_email) != null ){
            throw new DriverAlreadyExistsException("User already exists with given credentials");
        }else{
            return driverRepository.save(driver);
        }
    }

    @Override
    public void deleteDriver(Driver driver) {
        if (driver.getId() == null)
            throw new DriverNotFoundException("There is no id attached to driver");
        try{
            driverRepository.delete(driver);
        }catch (Exception ex){

        }
    }


    @Override
    public Boolean Login(String email, String password) {
        Driver driver; String driverpassword;
        try{
            driver = driverRepository.findByEmail(email);
            driverpassword = driver.getPassword();
        }catch (Exception e){
            throw new DriverNotFoundException("Driver not found with given credentials");
        }
            if ( driverpassword.equals(password)) {
                return true;
            }
            else {
                throw new WrongCredentialsException("Wrong Credentials");
            }

    }
}
