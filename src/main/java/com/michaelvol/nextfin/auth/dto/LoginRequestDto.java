package com.michaelvol.nextfin.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotNull @Size(min = 7, max = 28, message = "Username must be between 7 and 28 characters") String username,
        @NotNull @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters") String password
) {
}
