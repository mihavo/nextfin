package com.michaelvol.bankingapp.transaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record TransactionScheduleRequestDto(
        @NotNull @Future(message = "The scheduled transaction time must be in the future") Instant timestamp,
        @Valid TransferRequestDto transactionDetails) {
}
