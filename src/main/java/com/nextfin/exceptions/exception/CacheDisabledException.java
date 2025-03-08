package com.nextfin.exceptions.exception;

public class CacheDisabledException extends RuntimeException {

    public CacheDisabledException() {
        super();
    }

    public CacheDisabledException(String message) {
        super(message);
    }
}
