package com.michaelvol.nextfin.account.dto;

import com.michaelvol.nextfin.account.entity.Account;

import java.util.List;

public record GetMyAccountsResponseDto(List<Account> accounts, String message) {
}
