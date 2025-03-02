package com.nextfin.account.dto;

import com.nextfin.account.entity.Account;

import java.util.List;

public record GetMyAccountsResponseDto(List<Account> accounts, String message) {
}
