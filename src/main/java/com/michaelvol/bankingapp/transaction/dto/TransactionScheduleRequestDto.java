package com.michaelvol.bankingapp.transaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TransactionScheduleRequestDto(@NotNull Long accountId, @Valid TransferRequestDto transactionDetails) {
}
