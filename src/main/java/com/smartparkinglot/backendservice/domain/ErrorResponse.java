package com.smartparkinglot.backendservice.domain;



import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

@Data
public class ErrorResponse {
    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }

    private String message;
    private List<String> details;
}
