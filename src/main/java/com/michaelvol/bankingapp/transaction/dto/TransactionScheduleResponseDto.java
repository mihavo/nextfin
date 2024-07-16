package com.michaelvol.bankingapp.transaction.dto;

import java.util.UUID;

public record TransactionScheduleResponseDto(String message, UUID scheduleId) {
}
