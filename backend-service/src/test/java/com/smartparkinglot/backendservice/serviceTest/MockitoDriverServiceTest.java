package com.smartparkinglot.backendservice.serviceTest;

import com.smartparkinglot.backendservice.domain.Driver;
import com.smartparkinglot.backendservice.exceptions.AccountDeactivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.DriverRepository;
import com.smartparkinglot.backendservice.service.driverservice.DriverServiceImpl;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.MockitoAnnotations.*;
public class MockitoDriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    private List<Driver> drivers;

    @BeforeEach
    public void setup(){
        initMocks(this);
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
    public void shouldNotAddOrSaveBecauseAlreadyExists(){
        Throwable t = null;
        Driver caseDriver = new Driver(null,"pass1","email4","name1","surname1",LocalDateTime.now(),false);
        when(driverRepository.findByEmail(
                caseDriver.getEmail()))
                .thenReturn(caseDriver);
        try{
            Driver returnedDriver = driverService.addOrSave(caseDriver);
        }catch(AlreadyExistsException ex) {
            t = ex;
        }
        assertTrue(t instanceof AlreadyExistsException);

    }


}
