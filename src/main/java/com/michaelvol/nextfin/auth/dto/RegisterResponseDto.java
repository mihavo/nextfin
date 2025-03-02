package com.michaelvol.nextfin.auth.dto;

import com.michaelvol.nextfin.users.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegisterResponseDto(
        @NotNull String message,
        @NotNull @Valid User user
) {
}
