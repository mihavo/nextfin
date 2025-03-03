package com.nextfin.transaction.dto;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public record TransactionResponseDto(
        UUID transactionId,
        Long sourceAccountId,
        Long targetAccountId,
        Currency currency,
        String message,
        Instant timestamp
) {

}
