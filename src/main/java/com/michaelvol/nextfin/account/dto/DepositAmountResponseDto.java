package com.michaelvol.nextfin.account.dto;

import java.math.BigDecimal;

public record DepositAmountResponseDto(BigDecimal balance, String message) {
}
