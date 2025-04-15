package com.nextfin.account.service.core.impl;

import com.nextfin.account.dto.CreateAccountRequestDto;
import com.nextfin.account.dto.DepositAmountRequestDto;
import com.nextfin.account.dto.GetAccountBalanceDto;
import com.nextfin.account.dto.WithdrawAmountRequestDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountStatus;
import com.nextfin.account.enums.AccountType;
import com.nextfin.account.repository.AccountRepository;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.account.service.validator.AccountValidator;
import com.nextfin.employee.entity.Employee;
import com.nextfin.employee.service.EmployeeService;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.holder.entity.Holder;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.users.entity.NextfinUserDetails;
import com.nextfin.users.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Lazy private final AccountValidator accountValidator;

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
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getHolder() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, messageSource.getMessage("account.holder.not-found", null,
                                                                       LocaleContextHolder.getLocale()));
        }
        Employee manager = employeeService.getEmployeeById(dto.managerId);
        Account account = Account.builder()
                                 .balance(BigDecimal.ZERO)
                                 .status(AccountStatus.ACTIVE).holder(currentUser.getHolder())
                                 .manager(manager)
                                 .accountType(dto.accountType)
                                 .currency(Currency.getInstance(dto.currencyCode))
                                 .build();
        Account savedAccount = accountRepository.save(account);
        log.trace("Account created: {}", savedAccount);
        return savedAccount;
    }

    @Override
    public Account getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                                           .orElseThrow(() -> new NotFoundException(messageSource.getMessage(
                                                   "account.notfound",
                                                   new Long[]{accountId},
                                                   LocaleContextHolder.getLocale())));
        log.trace("Account fetched: {}", account);
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
        accountValidator.validateAccountOwnership(account);
        account.setTransaction2FAEnabled(!account.getTransaction2FAEnabled());
        accountRepository.save(account);
        return account.getTransaction2FAEnabled();
    }

    @Override
    public Boolean toggleTransactionSMSConfirmation(Account account) {
        accountValidator.validateAccountOwnership(account);
        account.setTransactionSMSConfirmationEnabled(!account.getTransactionSMSConfirmationEnabled());
        accountRepository.save(account);
        return account.getTransactionSMSConfirmationEnabled();
    }

    @Override
    public List<Account> getAccountsByUser(User owner, AccountType type) {
        Holder holder = owner.getHolder();
        if (holder == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    messageSource.getMessage("account.holder.not.found", null,
                            LocaleContextHolder.getLocale()));
        }
        return accountRepository.findAllByHolderAndAccountType(holder, type);
    }

    @Override
    public void updateDailyTotal(Transaction transaction) {
        Account sourceAccount = transaction.getSourceAccount();
        BigDecimal newTotal = sourceAccount.getDailyTotal().add(transaction.getAmount());
        sourceAccount.setDailyTotal(newTotal);
        accountRepository.save(sourceAccount);
    }

    @Override
    public List<Account> getCurrentUserAccounts() {
        UUID id = ((NextfinUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return accountRepository.getCurrentUserAccounts(id);
    }

}
