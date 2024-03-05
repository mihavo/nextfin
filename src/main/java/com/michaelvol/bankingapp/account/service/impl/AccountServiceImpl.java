package com.michaelvol.bankingapp.account.service.impl;

import java.math.BigDecimal;
import java.util.Currency;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import com.michaelvol.bankingapp.account.repository.AccountRepository;
import com.michaelvol.bankingapp.account.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(CreateAccountRequestDto createAccountDto) {
        Account account = Account.builder()
                                 .balance(BigDecimal.ZERO)
                                 .status(AccountStatus.ACTIVE)
                                 .currency(Currency.getInstance(createAccountDto.currencyCode))
                                 .build();
        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(EntityNotFoundException::new);
    }
}
