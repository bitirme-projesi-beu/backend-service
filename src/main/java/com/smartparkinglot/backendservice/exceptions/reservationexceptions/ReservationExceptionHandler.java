package com.smartparkinglot.backendservice.exceptions.reservationexceptions;

import com.smartparkinglot.backendservice.domain.ErrorResponse;
import com.smartparkinglot.backendservice.exceptions.driverexceptions.DriverAlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.driverexceptions.DriverNotFoundException;
import com.smartparkinglot.backendservice.exceptions.driverexceptions.WrongCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ReservationExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ActiveReservationExistsException.class)
    public final ResponseEntity<Object> handleAlreadyExistActiveReservationException(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Already have an active reservation!", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public final ResponseEntity<Object> handleReservationNotFoundException(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Reservation not found with ", details);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }
}