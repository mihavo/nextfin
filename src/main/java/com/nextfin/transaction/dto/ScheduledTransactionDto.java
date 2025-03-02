package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ScheduledTransactionDto {
    Transaction transaction;
    UUID scheduleId;
    LocalDateTime timestamp;
    String message;
}
