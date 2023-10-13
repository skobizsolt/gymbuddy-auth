package com.gymbuddy.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(ServiceExpection.class)
    public ResponseEntity<Object> handleServiceException(final ServiceExpection expection) {
        final Map<String, Object> response = new ConcurrentHashMap<>();
        response.put("errorCode", String.format("GYM_%03d", expection.getErrorCode()));
        response.put("message", expection.getMessage());
        response.put("timestamp:", Errors.getByCode(expection.getErrorCode()).getTimeStamp());
        return new ResponseEntity<>(response, expection.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleTransactionSystemException(Exception exception) {
        return handleServiceException(new ServiceExpection(Errors.UNEXPECTED_ERROR, exception.getMessage()));
    }
}
