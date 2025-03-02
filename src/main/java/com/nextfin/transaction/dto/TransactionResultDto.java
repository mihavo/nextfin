package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.Transaction;

public record TransactionResultDto(Transaction transaction, String message) {
}
