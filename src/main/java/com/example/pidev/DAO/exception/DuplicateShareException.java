package com.example.pidev.DAO.exception;

public class DuplicateShareException extends RuntimeException {
    public DuplicateShareException() {
    }

    public DuplicateShareException(String message) {
        super(message);
    }
}
