package com.nextfin.transaction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionSortingOptions {
    CREATED_AT("createdAt"),
    AMOUNT("amount"),
    ID("id"),
    CURRENCY("currency");

    private final String value;
}
