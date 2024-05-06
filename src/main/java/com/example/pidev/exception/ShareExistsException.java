package com.example.pidev.exception;

public class ShareExistsException extends RuntimeException {
    public ShareExistsException() {
    }

    public ShareExistsException(String message) {
        super(message);
    }
}
