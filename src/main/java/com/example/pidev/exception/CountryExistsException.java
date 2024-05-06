package com.example.pidev.exception;

public class CountryExistsException extends RuntimeException {
    public CountryExistsException() {
    }

    public CountryExistsException(String message) {
        super(message);
    }
}
