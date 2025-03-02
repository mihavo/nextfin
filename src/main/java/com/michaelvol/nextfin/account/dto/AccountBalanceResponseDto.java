package com.michaelvol.nextfin.account.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record AccountBalanceResponseDto(BigDecimal balance, Currency currency, String message) {
}
