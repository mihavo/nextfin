package com.michaelvol.bankingapp.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotNull String username,
        @NotNull String password
) {
}
