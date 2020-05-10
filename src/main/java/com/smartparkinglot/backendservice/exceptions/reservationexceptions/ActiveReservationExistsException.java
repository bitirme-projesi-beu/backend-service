package com.smartparkinglot.backendservice.exceptions.reservationexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ActiveReservationExistsException extends RuntimeException {
    public ActiveReservationExistsException(String message) {
        super(message);
    }
}
