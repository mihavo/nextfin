package com.michaelvol.bankingapp.account.service.core.impl;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.dto.DepositAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.GetAccountBalanceDto;
import com.michaelvol.bankingapp.account.dto.ValidateWithdrawalDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import com.michaelvol.bankingapp.account.repository.AccountRepository;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.account.service.validator.AccountValidator;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.service.EmployeeService;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.service.HolderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Lazy private final AccountValidator accountValidator;

    private final HolderService holderService;
    private final EmployeeService employeeService;

    private final MessageSource messageSource;

    @Override
    public void checkExistence(Long... accountIds) {
        Arrays.stream(accountIds).forEach(id -> {
            if (!accountRepository.existsById(id)) {
                throw new EntityNotFoundException(messageSource.getMessage("account.notfound",
                                                                           new Long[]{id},
                                                                           LocaleContextHolder.getLocale()));
            }
        });
    }

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
        Account savedAccount = accountRepository.save(account);
        log.debug("Account created: {}", savedAccount);
        return savedAccount;
    }

    @Override
    public Account getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                                           .orElseThrow(() -> new NotFoundException(messageSource.getMessage(
                                                   "account.notfound",
                                                   new Long[]{accountId},
                                                   LocaleContextHolder.getLocale())));
        return account;
    }

    @Override
    public List<Account> getAccounts(Long... accountIds) throws EntityNotFoundException {
        return accountRepository.findAllById(Arrays.asList(accountIds));
    }

    @Override
    public BigDecimal depositAmount(Long accountId, DepositAmountRequestDto dto) {
        Account account = getAccount(accountId);
        account.setBalance(account.getBalance().add(dto.amount()));
        accountRepository.save(account);
        log.trace("Account {} deposited with amount {}", account, dto.amount());
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
        log.trace("Account {} withdrawn with amount {}", account, withdrawAmount);
        return account.getBalance();
    }

    @Override
    public GetAccountBalanceDto checkBalance(Long accountId) {
        Account account = getAccount(accountId);
        return new GetAccountBalanceDto(account.getBalance(), account.getCurrency());
    }

    @Override
    public void validateWithdrawal(ValidateWithdrawalDto dto) {
        accountValidator.validateWithdrawal(dto);
    }

    @Override
    public void updateTransactionLimit(Account account, Long transactionLimit) {
        if (transactionLimit <= 0L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("account.transaction.limit.invalid", null,
                                                                       LocaleContextHolder.getLocale()));
        }
        if (!account.getTransactionLimitEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("account.transaction.limit.disabled", null,
                                                                       LocaleContextHolder.getLocale()));
        }
        account.setTransactionLimit(transactionLimit);
        accountRepository.save(account);
    }

    @Override
    public Boolean toggleTransactionLimit(Account account) {
        account.setTransactionLimitEnabled(!account.getTransactionLimitEnabled());
        accountRepository.save(account);
        return account.getTransactionLimitEnabled();
    }

    @Override
    public Boolean toggleTransaction2FA(Account account) {
        account.setTransaction2FAEnabled(!account.getTransaction2FAEnabled());
        accountRepository.save(account);
        return account.getTransaction2FAEnabled();
    }

}
