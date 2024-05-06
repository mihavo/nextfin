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
import jakarta.persistence.EntityNotFoundException;
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
import java.util.NoSuchElementException;
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
        Currency currency = dto.getCurrency();

        List<Account> accounts = accountService.checkExistence(sourceAccountId, targetAccountId);
        Account sourceAccount = accounts.get(0);
        Account targetAccount = accounts.get(1);

        if (sourceAccountId.equals(targetAccountId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("transaction.transfer.account.same", null,
                                                                       LocaleContextHolder.getLocale()));
        }
        accountService.validateWithdrawal(sourceAccount, amount, currency);

        Transaction transaction = Transaction.builder()
                                             .amount(amount)
                                             .currency(currency)
                                             .sourceAccount(sourceAccount)
                                             .targetAccount(targetAccount)
                                             .build();
        transactionRepository.save(transaction);
        processTransaction(transaction);
        return TransferResultDto.builder().transaction(transaction).message(
                messageSource.getMessage("transaction.transfer.processed",
                                         new UUID[]{transaction.getId()},
                                         LocaleContextHolder.getLocale())).build();
    }

    private void doTransaction(Transaction transaction) {
        Account sourceAccount = transaction.getSourceAccount();
        Account targetAccount = transaction.getTargetAccount();
        Currency currency = transaction.getCurrency();
        BigDecimal amount = transaction.getAmount();
        //Convert amount to target & source account's default currency
        CurrencyConversion requestToTargetConversion = MonetaryConversions.getConversion(targetAccount.getCurrency()
                                                                                                      .getCurrencyCode());
        CurrencyConversion requestToSourceConversion = MonetaryConversions.getConversion(sourceAccount.getCurrency()
                                                                                                      .getCurrencyCode());
        Money requestedCurrencyAmount = Money.of(amount, currency.getCurrencyCode());
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
        return transactionRepository.findById(transactionId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public TransactionStatus checkStatus(UUID transactionId) {
        return getTransaction(transactionId).getTransactionStatus();
    }

    @Override
    public Transaction processTransaction(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                                                       .orElseThrow(EntityNotFoundException::new);
        return processTransaction(transaction);
    }


    private Transaction processTransaction(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);
        doTransaction(transaction);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }
}
