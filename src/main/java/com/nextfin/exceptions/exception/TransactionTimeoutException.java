package com.nextfin.exceptions.exception;

public class TransactionTimeoutException extends RuntimeException {
    public TransactionTimeoutException() {
        super();
    }

    public TransactionTimeoutException(String message) {
        super(message);
    }
}
