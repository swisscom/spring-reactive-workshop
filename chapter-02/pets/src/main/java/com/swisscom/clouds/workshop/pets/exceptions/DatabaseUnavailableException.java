package com.swisscom.clouds.workshop.pets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Database not available")
public class DatabaseUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
}
