package com.nextfin.account.dto;

import com.nextfin.account.enums.AccountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

@Builder
public record CreateAccountResponseDto(Long id, String iban, BigDecimal balance, Currency currency, AccountType accountType,
                                       UUID holderId,
                                       Long transactionLimit, String friendlyName, Boolean transactionLimitEnabled,
                                       Boolean transaction2FAEnabled, Instant lastUpdated, Instant dateOpened) {
}
