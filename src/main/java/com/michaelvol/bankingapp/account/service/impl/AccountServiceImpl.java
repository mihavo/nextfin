package com.michaelvol.bankingapp.account.service.impl;

import com.michaelvol.bankingapp.account.dto.AccountCreationDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.repository.AccountRepository;
import com.michaelvol.bankingapp.account.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Override
    public Account createAccount(AccountCreationDto accountCreationDto) {

    }

    @Override
    public Account getAccount(Long accountId) {
        return null;
    }
}
