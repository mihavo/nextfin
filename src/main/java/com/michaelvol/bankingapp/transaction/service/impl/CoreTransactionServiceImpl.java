package com.michaelvol.bankingapp.transaction.service.impl;

import com.michaelvol.bankingapp.account.dto.DepositAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.AccountService;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.repository.TransactionRepository;
import com.michaelvol.bankingapp.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

@Service
@RequiredArgsConstructor
public class CoreTransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;

    private final MessageSource messageSource;

    @Override
    public TransferResultDto transferAmount(TransferRequestDto dto) {
        BigDecimal amount = dto.getAmount();
        Long sourceAccountId = dto.getSourceAccountId();
        Long targetAccountId = dto.getTargetAccountId();

        List<Account> accounts = accountService.checkExistence(sourceAccountId, targetAccountId);
        Account sourceAccount = accounts.get(0);
        Account targetAccount = accounts.get(1);

        if (sourceAccountId.equals(targetAccountId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("transaction.transfer.account.same", null,
                                                                       LocaleContextHolder.getLocale()));
        }

        doTransaction(sourceAccount, targetAccount, amount, dto.getCurrency());
        return null;
    }

    private void doTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, Currency requestedCurrency) {
        //Convert amount to target account's default currency
        CurrencyConversion requestToTargetConversion = MonetaryConversions.getConversion(targetAccount.getCurrency()
                                                                                                      .getCurrencyCode());
        CurrencyConversion requestToSourceConversion = MonetaryConversions.getConversion(sourceAccount.getCurrency()
                                                                                                      .getCurrencyCode());
        Money requestedCurrencyAmount = Money.of(amount, requestedCurrency.getCurrencyCode());
        Money targetCurrencyAmount = requestedCurrencyAmount.with(requestToTargetConversion); //amount in source account's currency
        Money sourceCurrencyAmount = requestedCurrencyAmount.with(requestToSourceConversion); //amount in target account's currency


        accountService.withdrawAmount(sourceAccount.getId(),
                                      new WithdrawAmountRequestDto(sourceCurrencyAmount.getNumber().numberValue(
                                              BigDecimal.class)));
        accountService.depositAmount(targetAccount.getId(),
                                     new DepositAmountRequestDto(targetCurrencyAmount.getNumber()
                                                                                     .numberValue(BigDecimal.class)));
    }

    @Override
    public Transaction getTransaction(UUID transactionId) {
        return null;
    }

    @Override
    public TransactionStatus checkTransactionStatus(UUID transactionId) {
        return null;
    }

    @Override
    public Transaction processTransaction(UUID transactionId) {
        return null;
    }
}
