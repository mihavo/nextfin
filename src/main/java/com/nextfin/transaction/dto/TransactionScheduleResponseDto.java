package com.nextfin.transaction.dto;

public record TransactionScheduleResponseDto(
        String message,
        ScheduledTransactionDto scheduledDetails
) {
}
