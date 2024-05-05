package com.michaelvol.bankingapp.transaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Builder
@Data
public class TransferRequestDto {
    @NotNull(message = "{transaction.transfer.account.notnull")
    Long sourceAccountId;

    @NotNull(message = "{transaction.transfer.account.notnull")
    Long targetAccountId;

    @NotNull(message = "{transaction.transfer.amount.notnull")
    @Positive(message = "{transaction.transfer.amount.positive")
    BigDecimal amount;

    @NotNull(message = "{transaction.transfer.account.currency")
    Currency currency;
}
