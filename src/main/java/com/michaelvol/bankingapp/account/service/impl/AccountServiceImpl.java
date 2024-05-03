package com.michaelvol.bankingapp.account.service.impl;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import com.michaelvol.bankingapp.account.repository.AccountRepository;
import com.michaelvol.bankingapp.account.service.AccountService;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.service.EmployeeService;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.service.HolderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final HolderService holderService;
    private final EmployeeService employeeService;

    @Override
    public Account createAccount(CreateAccountRequestDto dto) {
        Holder holder = holderService.getHolderById(dto.holderId);
        Employee manager = employeeService.getEmployeeById(dto.manager);

        Account account = Account.builder()
                                 .balance(BigDecimal.ZERO)
                                 .status(AccountStatus.ACTIVE)
                                 .holder(holder)
                                 .manager(manager)
                                 .accountType(dto.accountType)
                                 .currency(Currency.getInstance(dto.currencyCode))
                                 .build();

        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(EntityNotFoundException::new);
    }
}
