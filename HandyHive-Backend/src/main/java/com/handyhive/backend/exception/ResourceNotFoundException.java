package com.handyhive.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for cases where a requested resource is not found.
 * The @ResponseStatus annotation tells Spring to automatically return a 404 NOT_FOUND
 * status code whenever this exception is thrown and not caught elsewhere.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
