package com.michaelvol.nextfin.account.dto;

public record ToggleTransactionSMSConfirmationDto(Boolean transactionSMSConfirmationEnabled, String message) {
}
