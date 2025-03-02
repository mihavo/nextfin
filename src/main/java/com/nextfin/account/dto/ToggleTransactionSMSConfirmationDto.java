package com.nextfin.account.dto;

public record ToggleTransactionSMSConfirmationDto(Boolean transactionSMSConfirmationEnabled, String message) {
}
