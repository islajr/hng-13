package org.project.currencyexchangeapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.project.currencyexchangeapi.exception.exceptions.CountryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 404 - Country not found
    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCountryNotFoundException(CountryNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(handleException(ex.getMessage()));
    }

    // 405 -- Method Not Allowed
    @ExceptionHandler(value = MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(MethodNotAllowedException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(handleException("Method not allowed"));
    }

    // 404 -- No Resource Found
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(handleException("No resource found"));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(handleException(ex.getMessage()));
    }

    // 500 -- Assertion Error
    @ExceptionHandler(value = AssertionError.class)
    public ResponseEntity<ErrorResponse> handleAssertionError(AssertionError er) {
        log.error(er.getMessage(), er);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(handleException("Assertion failed"));
    }

    // 500 - General Fallback exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(handleException(ex.getMessage()));
    }

    ErrorResponse handleException(String message) {
        return new ErrorResponse(message);
    }
}
