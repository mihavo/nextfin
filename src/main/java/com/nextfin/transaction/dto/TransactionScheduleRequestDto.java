package com.nextfin.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TransactionScheduleRequestDto(
        @NotNull
        @Future(message = "The scheduled transaction time must be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss") LocalDateTime timestamp,
        @Valid TransferRequestDto transactionDetails) {
}
