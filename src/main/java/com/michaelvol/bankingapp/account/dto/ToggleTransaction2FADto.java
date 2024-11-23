package com.michaelvol.bankingapp.account.dto;

public record ToggleTransaction2FADto(Boolean transactionOTPEnabled, String message) {
}
