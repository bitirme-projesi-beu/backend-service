package com.smartparkinglot.backendservice.exceptions.driverexceptions;

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
public class DriverExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse err = new ErrorResponse("Server Error", details);
        return new ResponseEntity(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public final ResponseEntity<Object> handleDriverNotFoundException(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Driver not found", details);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DriverAlreadyExistsException.class)
    public final ResponseEntity<Object> handleDriverAlreadyExistsException(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Already exists", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public final ResponseEntity<Object> handleWrongCredentialsException(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Wrong credentials", details);
        return new ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
    }
}