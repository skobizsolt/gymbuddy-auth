package com.gymbuddy.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.gymbuddy.auth.exception.Errors.FORBIDDEN_BEHAVIOUR;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(ServiceExpection.class)
    public ResponseEntity<Object> handleServiceException(final ServiceExpection e) {
        final Errors serviceException = new Errors(
                e.getMessage()
        );
        return new ResponseEntity<>(serviceException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException() {
        Errors errorsDetails = new Errors(FORBIDDEN_BEHAVIOUR);
        return new ResponseEntity<>(errorsDetails, HttpStatus.FORBIDDEN);
    }
}
