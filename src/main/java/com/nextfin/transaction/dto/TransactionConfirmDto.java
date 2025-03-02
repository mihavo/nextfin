package com.nextfin.transaction.dto;

import java.util.UUID;

public record TransactionConfirmDto(UUID transactionId, String otp) {
}
