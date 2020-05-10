package com.smartparkinglot.backendservice.exceptions.driverexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DriverAlreadyExistsException extends RuntimeException {
    public DriverAlreadyExistsException(String message) {
    }
}
