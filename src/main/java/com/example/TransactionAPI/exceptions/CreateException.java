package com.example.TransactionAPI.exceptions;

public class CreateException extends RuntimeException {
    public CreateException(String message) {
        super(message);
    }
    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }
    public CreateException(Throwable cause) {
        super(cause);
    }
    public CreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {}
}
