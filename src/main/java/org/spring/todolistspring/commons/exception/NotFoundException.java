package org.spring.todolistspring.commons.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public NotFoundException(String message, HttpStatus status) {
        super(status, message);
    }
}
