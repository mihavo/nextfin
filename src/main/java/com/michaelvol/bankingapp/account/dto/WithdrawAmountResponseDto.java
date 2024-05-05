package com.michaelvol.bankingapp.account.dto;

import java.math.BigDecimal;

public record WithdrawAmountResponseDto(BigDecimal balance, String message) {
}
