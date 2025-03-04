package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.Transaction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ScheduledTransactionDto implements TransactionResponse {
    private final Transaction transaction;
    private final UUID scheduleId;
    private final LocalDateTime timestamp;
    private final String message;
}
