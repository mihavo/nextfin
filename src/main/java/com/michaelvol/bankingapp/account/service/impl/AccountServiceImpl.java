package com.michaelvol.bankingapp.account.service.impl;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.dto.DepositAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountRequestDto;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final HolderService holderService;
    private final EmployeeService employeeService;

    private final MessageSource messageSource;

    @Override
    public Account createAccount(CreateAccountRequestDto dto) {
        Holder holder = holderService.getHolderById(dto.holderId);
        Employee manager = employeeService.getEmployeeById(dto.managerId);

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

    @Override
    public BigDecimal depositAmount(Long accountId, DepositAmountRequestDto dto) {
        Account account = getAccount(accountId);
        account.setBalance(account.getBalance().add(dto.amount()));
        accountRepository.save(account);
        return account.getBalance();
    }

    @Override
    public BigDecimal withdrawAmount(Long accountId, WithdrawAmountRequestDto dto) {
        Account account = getAccount(accountId);
        BigDecimal balance = account.getBalance();
        BigDecimal withdrawAmount = dto.amount();
        if (balance.compareTo(withdrawAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("account.withdraw.insufficient", null,
                                                                       LocaleContextHolder.getLocale()));
        }
        account.setBalance(balance.subtract(withdrawAmount));
        accountRepository.save(account);
        return account.getBalance();
    }
}
