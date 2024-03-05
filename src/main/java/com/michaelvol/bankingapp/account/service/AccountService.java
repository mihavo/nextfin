package com.michaelvol.bankingapp.account.service;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;

public interface AccountService {
    Account createAccount(CreateAccountRequestDto createAccountRequestDto);

    Account getAccount(Long accountId);
}
