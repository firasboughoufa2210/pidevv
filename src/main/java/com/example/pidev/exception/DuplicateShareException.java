package com.example.pidev.exception;

public class DuplicateShareException extends RuntimeException {
    public DuplicateShareException() {
    }

    public DuplicateShareException(String message) {
        super(message);
    }
}
