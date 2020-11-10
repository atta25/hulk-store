package com.example.hulkstore.exception;

public class InvalidPurchaseOperationException extends RuntimeException {
    public InvalidPurchaseOperationException() {
        super("Purchase error");
    }
}
