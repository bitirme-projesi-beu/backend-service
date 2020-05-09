package com.smartparkinglot.backendservice.services.driverservice;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.repositories.DriverRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.net.HttpURLConnection;
import java.util.List;


@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {
    DriverRepository driverRepository;

    @Override
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return new ResponseEntity<List<Driver>>(driverRepository.findAll(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> getById(Long id) {
        return new ResponseEntity<Driver>(driverRepository.findById(id).get(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Driver> addOrSave(Driver driver) {
        Long driver_id = driver.getId();
        String driver_email = driver.getEmail();

        if (driver_id == null && driverRepository.findByEmail(driver_email) != null ){
            return new ResponseEntity(HttpStatus.CONFLICT); // 409
        }else{
            return new ResponseEntity(driverRepository.save(driver),HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity deleteDriver(Driver driver) {
        driverRepository.delete(driver);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity Login(String email, String password) {
        Driver driver; String driverpassword;
        try{
            driver = driverRepository.findByEmail(email);
            driverpassword = driver.getPassword();
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
            if ( driverpassword.equals(password)) {
                return new ResponseEntity(HttpStatus.ACCEPTED);
            }
            else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }

    }
}
