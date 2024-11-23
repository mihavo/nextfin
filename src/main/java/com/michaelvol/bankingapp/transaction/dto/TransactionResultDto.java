package com.michaelvol.bankingapp.transaction.dto;

import com.michaelvol.bankingapp.transaction.entity.Transaction;

public record TransactionResultDto(Transaction transaction, String message) {
}
