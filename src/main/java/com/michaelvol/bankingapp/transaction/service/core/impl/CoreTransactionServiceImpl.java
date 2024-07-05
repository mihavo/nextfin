package com.michaelvol.bankingapp.transaction.service.core.impl;

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
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

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

    public List<Transaction> getLatestSourceAccountTransactionsByDate(Account sourceAccount, Instant instant) {
        return transactionRepository.getTransactionsByAccountAndDate(sourceAccount, instant);
    }

    private @NotNull Transaction processTransaction(Transaction transaction) {
        return transactionProcessor.process(transaction);
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
