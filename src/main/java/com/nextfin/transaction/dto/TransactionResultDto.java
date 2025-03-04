package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.Transaction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TransactionResultDto implements TransactionResponse {
    private final Transaction transaction;
    private final String message;
}
