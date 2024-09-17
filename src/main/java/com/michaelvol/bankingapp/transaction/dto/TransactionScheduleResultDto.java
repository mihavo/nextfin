package com.michaelvol.bankingapp.transaction.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionScheduleResultDto(UUID transactionId, UUID scheduleId, LocalDateTime timestamp) {
}
