package com.michaelvol.bankingapp.transaction.dto;

public record TransactionScheduleResponseDto(
        String message,
        ScheduledTransactionDto scheduledDetails
) {
}
