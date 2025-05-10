package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.TransactionCategory;
import com.nextfin.transaction.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public record TransactionResponseDto(
        UUID transactionId,
        Long sourceAccountId,
        Long targetAccountId, Currency currency, BigDecimal amount,
        TransactionStatus status, TransactionCategory category,
        String message,
        Instant timestamp
) {

}
