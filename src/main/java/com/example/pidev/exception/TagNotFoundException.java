package com.example.pidev.exception;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException() {
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
