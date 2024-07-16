package com.michaelvol.bankingapp.transaction.dto;

public record TransactionScheduleRequestDto(Long accountId, TransferRequestDto transactionDetails) {
}
