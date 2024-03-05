package com.michaelvol.bankingapp.account.dto;

import com.michaelvol.bankingapp.account.entity.Account;

public record GetAccountResponseDto(Account account, String message) {
}
