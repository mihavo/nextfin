package com.michaelvol.nextfin.account.dto;

import com.michaelvol.nextfin.account.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Builder
@Getter
@Setter
public class ValidateWithdrawalDto {
    Account account;
    BigDecimal amount;
    Currency currency;
}
