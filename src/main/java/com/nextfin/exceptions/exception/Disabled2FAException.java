package com.nextfin.exceptions.exception;

public class Disabled2FAException extends RuntimeException {
    public Disabled2FAException(String message) {
        super(message);
    }

    public Disabled2FAException() {
        super();
    }
}
