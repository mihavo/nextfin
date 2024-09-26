package com.michaelvol.bankingapp.account.dto;

public record ToggleTransaction2FAResponseDto(Boolean transactionOTPEnabled, String message) {
}
