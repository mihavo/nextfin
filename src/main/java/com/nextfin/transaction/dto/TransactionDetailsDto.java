package com.nextfin.transaction.dto;

import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public record TransactionDetailsDto(UUID id, BigDecimal amount, Currency currency, Long sourceAccountId, Long targetAccountId,
                                    UUID sourceUserId, UUID targetUserId, String targetName,
                                    TransactionStatus status, TransactionType type,
                                    Instant createdAt, Instant updatedAt) {
}
