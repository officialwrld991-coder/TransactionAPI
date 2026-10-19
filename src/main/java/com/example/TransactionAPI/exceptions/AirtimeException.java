package com.example.TransactionAPI.exceptions;

public class AirtimeException extends RuntimeException {
    public AirtimeException(String message) {
        super(message);
    }

    public AirtimeException(String message, Throwable cause) {
        super(message, cause);
    }
    public AirtimeException(Throwable cause) {
        super(cause);
    }
    public AirtimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
