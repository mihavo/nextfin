package com.michaelvol.bankingapp.account.dto;

public record ToggleTransactionLimitResponseDto(Boolean transactionLimitEnabled, String message) {
}
