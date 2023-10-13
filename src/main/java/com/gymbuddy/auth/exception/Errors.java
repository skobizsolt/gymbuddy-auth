package com.gymbuddy.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public enum Errors {

    MISSING_TOKEN(101L, "Token not provided", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(102L, "The provided token is not valid!", HttpStatus.FORBIDDEN),
    USER_NOT_IDENTIFIED(103L, "User cannot identified", HttpStatus.UNAUTHORIZED),
    AUTHORIZATION_ERROR(104L, "Authorization failed during request process", HttpStatus.UNAUTHORIZED),
    UNEXPECTED_ERROR(999L, "Unexpected error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);

    private final Long errorCode;
    private final String cause;
    final HttpStatus httpStatus;
    private final ZonedDateTime timeStamp = ZonedDateTime.now();

    Errors(final Long errorCode, final String cause, final HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.cause = cause;
        this.httpStatus = httpStatus;
    }

    public static Errors getByCode(final Long value) {
        for (Errors errors : Errors.values()) {
            if (value.equals(errors.getErrorCode())) {
                return errors;
            }
        }
        return UNEXPECTED_ERROR;
    }
}
