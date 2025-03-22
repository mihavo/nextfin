package com.nextfin.exceptions.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CannotCacheException extends RuntimeException {
    public CannotCacheException() {
        super();
    }

    public CannotCacheException(String message) {
        super(message);
        log.error(message);

    }
}
