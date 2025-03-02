package com.nextfin.account.dto;

import java.math.BigDecimal;

public record WithdrawAmountResponseDto(BigDecimal balance, String message) {
}
