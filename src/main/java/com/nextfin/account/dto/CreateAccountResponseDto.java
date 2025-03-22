package com.nextfin.account.dto;

import com.nextfin.account.enums.AccountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Builder
public record CreateAccountResponseDto(Long id, BigDecimal balance, Currency currency, AccountType accountType, UUID holderId,
                                       Long transactionLimit) {
}
