package com.michaelvol.bankingapp.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginResponseDto(
        @NotNull String message
) {
}
