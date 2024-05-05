package com.michaelvol.bankingapp.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositAmountRequestDto(
        @NotNull(message = "{account.amount.notnull}") @Positive(message = "{account.amount.positive}") BigDecimal amount) {
}
