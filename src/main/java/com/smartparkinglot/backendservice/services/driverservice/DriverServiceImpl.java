package com.smartparkinglot.backendservice.services.driverservice;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.exceptions.AccountActivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.exceptions.WrongCredentialsException;
import com.smartparkinglot.backendservice.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
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
        Driver driver = driverRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Driver not found with id: "+id));


        return driver;

    }

    @Override
    public Driver addOrSave(Driver driver) {
        Long driver_id = driver.getId();
        String driver_email = driver.getEmail();

        if (driver_id == null && driverRepository.findByEmail(driver_email) != null ){
            Driver found_driver = driverRepository.findByEmail(driver_email);
            if (found_driver.getIsDeleted()){
                found_driver.setIsDeleted(false);
                driverRepository.save(found_driver);
                throw new AccountActivatedException("Account reactivated again.");
            }
            throw new AlreadyExistsException("User already exists with given credentials");
        }else{
            driver.setIsDeleted(false);
            return driverRepository.save(driver);
        }
    }

    @Override
    public void deleteDriver(Driver driver) {
        if (driver.getId() == null)
            throw new NotFoundException("There is no id attached to driver");
        try{
            driverRepository.deleteById(driver.getId());
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void setDeactive(Driver driver) {
        Driver driverb = driverRepository.findById(driver.getId()).get();
        driverb.setIsDeleted(true);
        driverRepository.save(driverb);
    }



    @Override
    public Driver Login(String email, String password) {
        Driver driver; String driverpassword;
        try{
            driver = driverRepository.findByEmail(email);
            driverpassword = driver.getPassword();
        }catch (Exception e){
            throw new NotFoundException("Driver not found with given credentials");
        }
            if (driver.getIsDeleted()) throw new NotFoundException("This account is deleted");
            if ( driverpassword.equals(password)) {
                return driver;
            }
            else {
                throw new WrongCredentialsException("Wrong Credentials");
            }

    }

    @Override
    public Boolean isExistsById(Long id) {
        return driverRepository.existsById(id);
    }

}
