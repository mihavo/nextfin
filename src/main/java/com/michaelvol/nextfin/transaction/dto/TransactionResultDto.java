package com.michaelvol.nextfin.transaction.dto;

import com.michaelvol.nextfin.transaction.entity.Transaction;

public record TransactionResultDto(Transaction transaction, String message) {
}
