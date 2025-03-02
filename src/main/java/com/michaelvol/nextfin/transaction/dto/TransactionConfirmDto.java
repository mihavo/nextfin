package com.michaelvol.nextfin.transaction.dto;

import java.util.UUID;

public record TransactionConfirmDto(UUID transactionId, String otp) {
}
