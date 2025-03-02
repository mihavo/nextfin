package com.nextfin.account.dto;

public record ToggleTransaction2FADto(Boolean transactionOTPEnabled, String message) {
}
