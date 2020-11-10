package com.example.hulkstore.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("Invalid user");
    }

    public InvalidUserException(Exception e) {
        super("Invalid user", e);
    }
}
