package com.nextfin.auth.dto;

import com.nextfin.users.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegisterResponseDto(
        @NotNull String message,
        @NotNull @Valid User user
) {
}
