package com.michaelvol.bankingapp.account.dto;

import com.michaelvol.bankingapp.account.entity.Account;

import java.util.List;

public record GetMyAccountsResponseDto(List<Account> accounts, String message) {
}
