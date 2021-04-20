package com.swisscom.clouds.workshop.orchestrator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Pet service not available")
public class PetServiceUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
}
