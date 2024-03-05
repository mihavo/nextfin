package com.michaelvol.bankingapp.account.service;

import com.michaelvol.bankingapp.account.dto.AccountCreationDto;
import com.michaelvol.bankingapp.account.entity.Account;

public interface AccountService {
    Account createAccount(AccountCreationDto accountCreationDto);

    Account getAccount(Long accountId);
}
