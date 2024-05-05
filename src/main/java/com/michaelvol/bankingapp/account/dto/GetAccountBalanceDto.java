package com.michaelvol.bankingapp.account.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Currency;

public record GetAccountBalanceDto(@NotNull BigDecimal balance, @NotNull Currency currency) {
}
