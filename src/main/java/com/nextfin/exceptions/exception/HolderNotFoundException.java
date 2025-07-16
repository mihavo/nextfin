package com.nextfin.exceptions.exception;

import java.util.UUID;

public class HolderNotFoundException extends RuntimeException {
    public HolderNotFoundException() {
    }

    public HolderNotFoundException(UUID userId) {
        super("User with ID: " + userId + " does not have any holders");
    }
}
