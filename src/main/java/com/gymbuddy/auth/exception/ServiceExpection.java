package com.gymbuddy.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ServiceExpection extends RuntimeException{

    private final Long errorCode;
    private final HttpStatus statusCode;

    public ServiceExpection(Throwable cause) {
        this(null, null, cause);
    }

    public ServiceExpection(Errors error) { this(error, null, null); }

    public ServiceExpection(Errors error, String message) {
        this(error, message, null);
    }

    public ServiceExpection(Errors error, String message, Throwable cause) {
        this(error.getCause() + (message == null ? "" : " ["+ message +"]"),
                cause,
                error.getErrorCode(),
                error.getHttpStatus());
    }

    public ServiceExpection(Errors error, Throwable cause) {
        this(error, null, cause);
    }

    /**
     * Constructs a new service exception with the specified detail
     * message, cause, status code.
     *
     * @param message   the detail message.
     * @param cause     the cause.  (A {@code null} value is permitted,
     *                  and indicates that the cause is nonexistent or unknown.)
     */
    protected ServiceExpection(String message, Throwable cause, Long errorCode, HttpStatus statusCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
