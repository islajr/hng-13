package org.project.dynamicprofile.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponse(
    int httpStatus,
    String error,
    String message,
    String path,
    Instant timestamp
) {
     public ErrorResponse(int httpStatus, String error, String message, String path, Instant timestamp) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public ErrorResponse(HttpStatus httpStatus, String message, String path, Instant timestamp) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), message, path, timestamp);
    }
}
