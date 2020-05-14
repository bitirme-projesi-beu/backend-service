package com.smartparkinglot.backendservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class AccountActivatedException extends RuntimeException {
    public AccountActivatedException(String message) {
        super(message);
    }
}
