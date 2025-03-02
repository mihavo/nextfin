package com.michaelvol.nextfin.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginResponseDto(
        @NotNull String message
) {
}
