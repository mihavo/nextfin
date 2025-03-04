package com.nextfin.transaction.dto;

import com.nextfin.transaction.enums.TransactionStatus;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public record TransactionResponseDto(
        UUID transactionId,
        Long sourceAccountId,
        Long targetAccountId,
        Currency currency,
        TransactionStatus status,
        String message,
        Instant timestamp
) {

}
