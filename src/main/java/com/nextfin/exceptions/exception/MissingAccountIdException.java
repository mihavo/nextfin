package com.nextfin.exceptions.exception;

public class MissingAccountIdException extends RuntimeException {

    public MissingAccountIdException() {
        super("Account ID must not be null when generating IBAN");
    }
}
