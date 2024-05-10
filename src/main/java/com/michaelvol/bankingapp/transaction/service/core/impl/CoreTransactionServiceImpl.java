package com.michaelvol.bankingapp.transaction.service.core.impl;

import com.michaelvol.bankingapp.account.dto.DepositAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.transaction.dto.GetTransactionOptions;
import com.michaelvol.bankingapp.transaction.dto.TransactionDirection;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.repository.TransactionRepository;
import com.michaelvol.bankingapp.transaction.service.core.TransactionService;
import com.michaelvol.bankingapp.transaction.service.processor.TransactionProcessor;
import com.michaelvol.bankingapp.transaction.service.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CoreTransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProcessor transactionProcessor;
    @Lazy private final TransactionValidator transactionValidator;

    private final AccountService accountService;

    private final MessageSource messageSource;

    @Override
    public TransferResultDto transferAmount(TransferRequestDto dto) {
        transactionValidator.validate(dto);
        Transaction transaction = storeTransaction(dto);
        processTransaction(transaction);
        return TransferResultDto.builder().transaction(transaction)
                                .message(messageSource.getMessage("transaction.transfer.processed",
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
        return transactionRepository.findById(transactionId)
                                    .orElseThrow(getNotFoundExceptionSupplier(transactionId));
    }

    @Override
    public TransactionStatus checkStatus(UUID transactionId) {
        return getTransaction(transactionId).getTransactionStatus();
    }

    @Override
    public Transaction processTransaction(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                                                       .orElseThrow(getNotFoundExceptionSupplier(transactionId));
        return processTransaction(transaction);
    }

    @Override
    public Page<Transaction> getAccountTransactions(Account account, GetTransactionOptions options) {
        TransactionDirection direction = options.getDirection();
        PageRequest pageRequest = PageRequest.of(options.getSkip(),
                                                 options.getPageSize(),
                                                 Sort.by(options.getSortDirection(),
                                                         options.getSortBy().getValue()));
        switch (direction) {
            case INCOMING -> {
                return transactionRepository.findByTargetAccount(account, pageRequest);
            }
            case OUTGOING -> {
                return transactionRepository.findBySourceAccount(account, pageRequest);
            }
            default -> {
                return transactionRepository.findBySourceAccountOrTargetAccount(account, account, pageRequest);
            }
        }
    }

    public List<Transaction> getLatestSourceAccountTransactionsByDate(Account sourceAccount, LocalDateTime date) {
        return transactionRepository.getTransactionsByAccountAndDate(sourceAccount, date);
    }

    private Transaction processTransaction(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);
        doTransaction(transaction);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    private @NotNull Transaction storeTransaction(TransferRequestDto dto) {
        Transaction transaction = Transaction.builder()
                                             .amount(dto.getAmount())
                                             .currency(dto.getCurrency())
                                             .sourceAccount(accountService.getAccount(dto.getSourceAccountId()))
                                             .targetAccount(accountService.getAccount(dto.getTargetAccountId()))
                                             .build();
        transactionRepository.save(transaction);
        return transaction;
    }

    private @NotNull Supplier<NotFoundException> getNotFoundExceptionSupplier(UUID transactionId) {
        return () -> new NotFoundException(messageSource.getMessage(
                "transaction.notfound",
                new UUID[]{transactionId},
                LocaleContextHolder.getLocale()));
    }
}
