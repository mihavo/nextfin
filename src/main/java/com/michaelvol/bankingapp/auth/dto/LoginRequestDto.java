package com.michaelvol.bankingapp.auth.dto;

public record LoginRequestDto(
        String username,
        String password
) {
}
