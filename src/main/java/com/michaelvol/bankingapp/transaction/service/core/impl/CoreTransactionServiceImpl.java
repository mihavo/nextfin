package com.michaelvol.bankingapp.transaction.service.core.impl;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.messaging.transaction.service.TransactionConfirmationService;
import com.michaelvol.bankingapp.transaction.dto.*;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.enums.TransactionType;
import com.michaelvol.bankingapp.transaction.repository.TransactionRepository;
import com.michaelvol.bankingapp.transaction.scheduler.TransactionScheduler;
import com.michaelvol.bankingapp.transaction.service.core.TransactionService;
import com.michaelvol.bankingapp.transaction.service.processor.TransactionProcessor;
import com.michaelvol.bankingapp.transaction.service.security.TransactionSecurityService;
import com.michaelvol.bankingapp.transaction.service.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CoreTransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProcessor transactionProcessor;

    @Lazy private final TransactionValidator transactionValidator;
    @Lazy private final TransactionScheduler transactionScheduler;
    private final TransactionSecurityService securityService;

    private final AccountService accountService;
    private final TransactionConfirmationService confirmationService;

    private final MessageSource messageSource;

    @Override
    public TransactionResultDto initiateTransaction(TransferRequestDto dto) {
        transactionValidator.validate(dto);
        Transaction transaction = storeTransaction(dto, TransactionType.INSTANT, null);
        return handleTransactionWith2FACheck(transaction);
    }


    @Override
    public TransactionResultDto initiateScheduledTransaction(TransactionScheduleRequestDto dto) {
        transactionValidator.validate(dto.transactionDetails());
        Transaction transaction = storeTransaction(dto.transactionDetails(),
                                                   TransactionType.SCHEDULED,
                                                   dto.timestamp());
        return handleTransactionWith2FACheck(transaction);
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
        return transactionRepository.getTransactionsByAccountAndDate(sourceAccount.getId(), instant);
    }

    @Override
    public Transaction confirmTransaction(TransactionConfirmDto dto) {
        Transaction transaction = getTransaction(dto.transactionId());
        String sourcePhone = transaction.getSourceAccount().getHolder().getUser().getPreferredPhoneNumber();
        securityService.validateOTP(sourcePhone, dto.otp());
        return transaction;
    }


    public @NotNull ScheduledTransactionDto scheduleTransaction(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.SCHEDULED) {
            throw new IllegalArgumentException("Transaction must be of type SCHEDULED");
        }
        return transactionScheduler.scheduleTransaction(transaction);
    }


    public @NotNull TransactionResultDto processTransaction(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.INSTANT) {
            throw new IllegalArgumentException("Transaction must be of type INSTANT");
        }
        Transaction processedTransaction = transactionProcessor.process(transaction);
        return finalizeTransaction(transaction, processedTransaction);
    }


    @Override
    public TransactionResultDto processScheduledTransaction(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.SCHEDULED) {
            throw new IllegalArgumentException("Transaction must be of type SCHEDULED");
        }
        Transaction processedTransaction = transactionProcessor.process(transaction);
        return finalizeTransaction(transaction, processedTransaction);
    }


    private @NotNull Transaction storeTransaction(TransferRequestDto dto, TransactionType type, LocalDateTime scheduledAt) {
        Transaction transaction = Transaction.builder()
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .transactionStatus(TransactionStatus.CREATED)
                .transactionType(type)
                .scheduledAt(scheduledAt)
                .sourceAccount(accountService.getAccount(dto.getSourceAccountId()))
                .targetAccount(accountService.getAccount(dto.getTargetAccountId()))
                .build();
        transactionRepository.save(transaction);
        return transaction;
    }

    @NotNull
    private TransactionResultDto finalizeTransaction(Transaction transaction, Transaction processedTransaction) {
        String recipient = transaction.getTargetAccount().getHolder().getUser().getPreferredPhoneNumber();
        confirmationService.sendSMS(recipient, processedTransaction);
        return new TransactionResultDto(processedTransaction, messageSource.getMessage(
                "transaction.transfer.processed", new UUID[]{transaction.getId()}, LocaleContextHolder.getLocale()));
    }

    private @NotNull Supplier<NotFoundException> getNotFoundExceptionSupplier(UUID transactionId) {
        return () -> new NotFoundException(messageSource.getMessage(
                "transaction.notfound",
                new UUID[]{transactionId},
                LocaleContextHolder.getLocale()));
    }

    private TransactionResultDto handleTransactionWith2FACheck(Transaction transaction) {
        if (transaction.getSourceAccount().getTransaction2FAEnabled()) {
            securityService.sendOTP(transaction);
            return new TransactionResultDto(transaction, messageSource.getMessage(
                    "transaction.transfer.awaiting-validation",
                    new UUID[]{transaction.getId()},
                    LocaleContextHolder.getLocale()));
        } else {
            return processTransaction(transaction);
        }
    }
}
