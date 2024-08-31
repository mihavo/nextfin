package com.michaelvol.bankingapp.auth.dto;

import com.michaelvol.bankingapp.users.dto.CreateUserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDto(
        @Valid
        @NotNull(message = "User data must be provided")
        CreateUserDto user) {
}
