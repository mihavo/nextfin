package com.nextfin.transaction.service.core.impl;

import com.nextfin.account.entity.Account;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.exceptions.exception.CannotCacheException;
import com.nextfin.exceptions.exception.Disabled2FAException;
import com.nextfin.exceptions.exception.UserNotFoundException;
import com.nextfin.transaction.dto.*;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.enums.TransactionType;
import com.nextfin.transaction.repository.TransactionRepository;
import com.nextfin.transaction.scheduler.TransactionScheduler;
import com.nextfin.transaction.service.cache.TransactionCacheService;
import com.nextfin.transaction.service.confirmation.ConfirmationService;
import com.nextfin.transaction.service.core.TransactionService;
import com.nextfin.transaction.service.processor.TransactionProcessor;
import com.nextfin.transaction.service.security.MFATransactionService;
import com.nextfin.transaction.service.security.TransactionSecurityService;
import com.nextfin.transaction.service.utils.TransactionUtils;
import com.nextfin.transaction.service.validator.TransactionValidator;
import com.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CoreTransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionProcessor transactionProcessor;

    private final TransactionUtils transactionUtils;

    private final TransactionCacheService cache;

    private final TransactionSecurityService security;

    @Lazy
    private final TransactionValidator transactionValidator;
    @Lazy
    private final TransactionScheduler transactionScheduler;

    private final Optional<MFATransactionService> mfaTransactionService;

    private final AccountService accountService;

    private final ConfirmationService confirmationService;

    private final MessageSource messageSource;

    private final UserService userService;


    @Override
    public TransactionResponse initiateTransaction(TransferRequestDto dto) {
        transactionValidator.validate(dto);
        Transaction transaction = storeTransaction(dto, TransactionType.INSTANT, null);
        return handleTransaction(transaction);
    }


    @Override
    public TransactionResponse initiateScheduledTransaction(TransactionScheduleRequestDto dto) {
        transactionValidator.validate(dto.transactionDetails());
        Transaction transaction = storeTransaction(dto.transactionDetails(),
                                                   TransactionType.SCHEDULED,
                                                   dto.timestamp());
        return handleScheduledTransaction(transaction);
    }


    @Override
    public Transaction getTransaction(UUID transactionId) {
        Set<String> recentIds = cache.fetchCacheRecents();
        Transaction transaction = recentIds.stream().filter(
                id -> id.equals(transactionId.toString())).findAny().flatMap(
                id -> cache.fetchFromCache(transactionId)).orElseGet(
                () -> transactionRepository.findById(transactionId).orElseThrow(
                        transactionUtils.getNotFoundExceptionSupplier(transactionId)));
        security.evaluatePermissions(transaction);
        return transaction;
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
                                                 Sort.by(options.getSortDirection(), options.getSortBy().getValue()));
        switch (direction) {
            case INCOMING -> {
                return transactionRepository.findByTargetAccount(account, pageRequest);
            }
            case OUTGOING -> {
                //Attempt to fetch recents from cache,
                // with repository fallback if page size is bigger than cache size / page skip is provided.
                List<Transaction> recents = fetchRecentsFromCache(options);
                return recents.isEmpty() ? transactionRepository.findBySourceAccount(account, pageRequest) : new PageImpl<>(
                        recents);
            }
            default -> {
                List<Transaction> targets = transactionRepository.findByTargetAccount(account, pageRequest).getContent();
                List<Transaction> recents = fetchRecentsFromCache(options);
                if (recents.isEmpty()) return transactionRepository.findBySourceAccountOrTargetAccount(account, account,
                                                                                                       pageRequest);
                recents.addAll(targets);
                return new PageImpl<>(recents);
            }
        }
    }

    private List<Transaction> fetchRecentsFromCache(GetTransactionOptions options) {
        Set<String> recents = cache.fetchCacheRecents();
        if (options.getPageSize() > recents.size() || options.getSkip() > 0) return List.of();
        return recents.stream().map(recent -> cache.fetchFromCache(UUID.fromString(recent))).flatMap(Optional::stream).sorted(
                Comparator.comparing(Transaction::getCreatedAt)).limit(options.getPageSize()).collect(Collectors.toList());
    }

    public List<Transaction> getLatestSourceAccountTransactionsByDate(Account sourceAccount, Instant instant) {
        return transactionRepository.getTransactionsByAccountAndDate(sourceAccount.getId(), instant);
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Transaction confirmTransaction(TransactionConfirmDto dto) {
        Transaction transaction = getTransaction(dto.transactionId());
        Account sourceAccount = transaction.getSourceAccount();
        String sourcePhone = sourceAccount.getHolder().getUser().getPreferredPhoneNumber();
        if (!transactionUtils.check2fa(sourceAccount)) {
            throw new Disabled2FAException("Attempted transaction confirmation on disabled 2FA config.");
        }
        mfaTransactionService.get().validateOTP(sourcePhone, dto.otp());
        return transaction;
    }

    @Override
    public @NotNull ScheduledTransactionDto scheduleTransaction(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.SCHEDULED) {
            throw new IllegalArgumentException("Transaction must be of type SCHEDULED");
        }
        return transactionScheduler.scheduleTransaction(transaction);
    }


    public @NotNull TransactionResponse processTransaction(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.INSTANT) {
            throw new IllegalArgumentException("Transaction must be of type INSTANT");
        }
        Transaction processedTransaction = transactionProcessor.process(transaction);
        return finalizeTransaction(transaction, processedTransaction);
    }


    @Override
    public TransactionResponse processScheduledTransaction(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.SCHEDULED) {
            throw new IllegalArgumentException("Transaction must be of type SCHEDULED");
        }
        Transaction processedTransaction = transactionProcessor.process(transaction);
        return finalizeTransaction(transaction, processedTransaction);
    }


    private @NotNull Transaction storeTransaction(TransferRequestDto dto, TransactionType type,
                                                  LocalDateTime scheduledAt) {
        Transaction transaction = Transaction.builder().amount(dto.getAmount()).currency(
                dto.getCurrency()).transactionStatus(TransactionStatus.CREATED).transactionType(type).scheduledAt(
                scheduledAt).sourceAccount(accountService.getAccount(dto.getSourceAccountId())).targetAccount(
                accountService.getAccount(dto.getTargetAccountId())).build();
        Transaction saved = transactionRepository.save(transaction);
        try {
            cache.cacheTransaction(userService.getCurrentUser().getId(), saved);
        } catch (UserNotFoundException e) {
            throw new CannotCacheException("User not found, cannot cache transaction " + transaction.getId());
        }
        return saved;
    }

    @NotNull
    private TransactionResponse finalizeTransaction(Transaction transaction, Transaction processedTransaction) {
        confirmationService.handleConfirmation(transaction.getSourceAccount(), processedTransaction);
        return new TransactionResultDto(processedTransaction, messageSource.getMessage("transaction.transfer.processed",
                                                                                       new UUID[]{transaction.getId()},
                                                                                       LocaleContextHolder.getLocale()));
    }


    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private TransactionResponse handleTransaction(Transaction transaction) {
        if (transactionUtils.check2fa(transaction.getSourceAccount())) {
            mfaTransactionService.get().sendOTP(transaction);
            return new TransactionResultDto(transaction, messageSource.getMessage(
                    "transaction.transfer" + ".awaiting" + "-validation", new UUID[]{transaction.getId()},
                    LocaleContextHolder.getLocale()));
        } else {
            return processTransaction(transaction);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private ScheduledTransactionDto handleScheduledTransaction(Transaction transaction) {
        if (transactionUtils.check2fa(transaction.getSourceAccount())) {
            mfaTransactionService.get().sendOTP(transaction);
            return ScheduledTransactionDto.builder().transaction(transaction).message(messageSource.getMessage(
                    "transaction.transfer.awaiting-validation",
                    new UUID[]{transaction.getId()},
                    LocaleContextHolder.getLocale())).build();
        }
        return scheduleTransaction(transaction);
    }


}
