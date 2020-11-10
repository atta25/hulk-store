package com.example.hulkstore.exception;

public class InvalidProductOperationException extends RuntimeException {
    public InvalidProductOperationException(String message, Exception e) {
        super(message, e);
    }
}