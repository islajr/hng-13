package org.project.currencyexchangeapi.exception.exceptions;

public class CountryNotFoundException extends RuntimeException {

    String message;

    public CountryNotFoundException(String message) {
        super(message);
    }
}
