package com.nextfin.account.dto;

import jakarta.validation.constraints.Positive;

public record UpdateTransactionLimitRequestDto(
        @Positive(message = "{account.transaction.limit.invalid}") Long transactionLimit) {
}
