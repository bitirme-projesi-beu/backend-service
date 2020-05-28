package com.smartparkinglot.backendservice.serviceTest;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.exceptions.AccountDeactivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.DriverRepository;
import com.smartparkinglot.backendservice.service.driverservice.DriverService;
import com.smartparkinglot.backendservice.service.driverservice.DriverServiceImpl;
import mockit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.IsInstanceOf.any;
import static org.junit.jupiter.api.Assertions.*;
public class DriverServiceTest {

    @Injectable
    private DriverRepository driverRepository;

    @Tested
    private DriverServiceImpl driverService;

    private List<Driver> drivers;

    @BeforeEach
    public void setup(){
        drivers = new ArrayList<Driver>();
        LocalDateTime now = LocalDateTime.now();
        Driver d1 = new Driver(0L,"pass1","email11","name1","surname1",LocalDateTime.now(),false);
        Driver d2 = new Driver(1L,"pass2","email12","name2","surname2",LocalDateTime.now(),false);
        Driver d3 = new Driver(2L,"pass3","email3","name3","surname3",LocalDateTime.now(),true);
        Driver d4 = new Driver(null,"pass1","email4","name1","surname1",LocalDateTime.now(),false);

        drivers.add(d1);
        drivers.add(d2);
        drivers.add(d3);
        drivers.add(d4);
    }

    @Test
    public void shouldGetAllDrivers(){
        new Expectations(){{
           driverRepository.findAll();
           result = drivers;
           times = 1;
        }};
        List<Driver> driversFromService = driverService.getAllDrivers();
        assertEquals(drivers.size(),driversFromService.size());
    }

    @Test
    public void shouldGetDriverById(){
        new Expectations(){{
            driverRepository.findById(0L);
            result = drivers.get(0);
            times = 1;
        }};

        Driver driver = driverService.getById(0L);

        assertEquals(drivers.get(0),driver);
    }

    @Test
    public void shouldNotGetDriverById() throws NotFoundException {
        Throwable t = null;
        new Expectations(){{
            driverRepository.findById(anyLong);
            result = Optional.empty();
            times = 1;
        }};
        try{
            Driver driver = driverService.getById(2L);
        }catch (NotFoundException n){
            t = n;
        }
        assertTrue(t instanceof NotFoundException);

}

    @Test
    public void shouldAddOrSave(){
        new Expectations(){{
            driverRepository.save(withInstanceOf(Driver.class));
            result = drivers.get(0);
            times = 1;
        }};
        Driver returnedDriver = driverService.addOrSave(drivers.get(0));
        assertEquals(0L,returnedDriver.getId().longValue());
    }
    @Test
    public void shouldNotAddOrSaveBecauseAlreadyExists() throws AlreadyExistsException{
        Driver caseDriver = new Driver(null,"pass1","email4","name1","surname1",LocalDateTime.now(),false);
        Throwable t = null;
        new Expectations(){{
           driverRepository.isExistsByEmail(caseDriver.getEmail());
           result = true;
           times = 1;
        }};

        try{
            Driver returnedDriver = driverService.addOrSave(caseDriver);
        }catch(AlreadyExistsException ex) {
            t = ex;
        }
        assertTrue(t instanceof AlreadyExistsException);

    }

    @Test
    public void shouldNotAddOrSaveBecauseItIsDeactivated(){
        Driver caseDriver = new Driver(null,"pass1","email4","name1","surname1",LocalDateTime.now(),true);
        Throwable t = null;
        new Expectations(){{
            driverRepository.isExistsByEmail(caseDriver.getEmail());
            result = true;
            times = 1;

            driverRepository.findByEmail(caseDriver.getEmail()).getIsDeleted();
            result = true;
            times = 1;
        }};
        try {
            Driver returned_driver = driverService.addOrSave(caseDriver);
        }catch (AccountDeactivatedException ex){
            t = ex;
        }
        assertTrue(t instanceof AccountDeactivatedException);
    }


}
