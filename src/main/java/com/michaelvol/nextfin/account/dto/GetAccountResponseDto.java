package com.michaelvol.nextfin.account.dto;

import com.michaelvol.nextfin.account.entity.Account;

public record GetAccountResponseDto(Account account, String message) {
}
