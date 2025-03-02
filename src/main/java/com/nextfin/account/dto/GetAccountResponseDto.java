package com.nextfin.account.dto;

import com.nextfin.account.entity.Account;

public record GetAccountResponseDto(Account account, String message) {
}
