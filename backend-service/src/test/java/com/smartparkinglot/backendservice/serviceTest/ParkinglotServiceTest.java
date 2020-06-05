package com.smartparkinglot.backendservice.serviceTest;

import com.smartparkinglot.backendservice.domain.ParkingLot;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.ParkingLotRepository;
import com.smartparkinglot.backendservice.service.accountservice.AccountServiceImpl;
import com.smartparkinglot.backendservice.service.parkinglotservice.ParkingLotServiceImpl;
import com.smartparkinglot.backendservice.service.ticketservice.TicketService;
import com.smartparkinglot.backendservice.service.ticketservice.TicketServiceImpl;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class ParkinglotServiceTest {

    @Tested
    private ParkingLotServiceImpl parkingLotService;
    @Injectable
    private ParkingLotRepository parkingLotRepository;
    @Injectable
    private TicketServiceImpl ticketService;
    @Injectable
    private AccountServiceImpl accountService;
    private List<ParkingLot> parkingLots;

    @BeforeEach
    public void setup(){
        parkingLots = new ArrayList<>();
    }

    @Test
    public void shouldGetAllParkingLots(){
        ParkingLot p1 = new ParkingLot(1L,1L,"PL1",5.50,30,30,40.22,30.22,4.0,false);
        parkingLots.add(p1);

        new Expectations(){{
            parkingLotRepository.findAll();
            result = parkingLots;
            times = 1;
        }};

        List<ParkingLot> testedParkingLots = parkingLotService.getAllParkingLots();

        assertEquals(parkingLots.size(),testedParkingLots.size());
    }
    @Test
    public void shouldGetById(){
        ParkingLot p1 = new ParkingLot(1L,1L,"PL1",5.50,30,null,40.22,30.22,null,false);
        ParkingLot p2 = new ParkingLot(1L,1L,"PL1",5.50,30,30,40.22,30.22,null,false);
        new Expectations(){{
            parkingLotRepository.findById(anyLong);
            result = p2;
            times = 1;
        }};

        ParkingLot testParkingLot = parkingLotService.getById(p1.getId());
        assertEquals(p2.getActiveCapacity(),testParkingLot.getActiveCapacity());
        assertEquals(p2,testParkingLot);
    }
    @Test
    public void shouldNotGetByIdBecauseNotFound() throws NotFoundException{
        ParkingLot p1 = new ParkingLot(1L,1L,"PL1",5.50,30,null,40.22,30.22,null,false);
        Throwable t = null;
        new Expectations(){{
            parkingLotRepository.findById(anyLong);
            result = Optional.empty();
        }};
        try {
            ParkingLot parkingLot  = parkingLotService.getById(1L);
        }catch (NotFoundException n){
            t = n;
        }
        assertTrue(t instanceof NotFoundException);
    }

}
