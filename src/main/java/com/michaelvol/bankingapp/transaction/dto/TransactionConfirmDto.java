package com.michaelvol.bankingapp.transaction.dto;

import java.util.UUID;

public record TransactionConfirmDto(UUID transactionId, String otp) {
}
