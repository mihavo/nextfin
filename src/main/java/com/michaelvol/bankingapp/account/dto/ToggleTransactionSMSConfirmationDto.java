package com.michaelvol.bankingapp.account.dto;

public record ToggleTransactionSMSConfirmationDto(Boolean transactionSMSConfirmationEnabled, String message) {
}
