package com.nextfin.transaction.service.core.impl;

import com.nextfin.account.entity.Account;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.cache.CacheService;
import com.nextfin.cache.utils.CacheUtils;
import com.nextfin.exceptions.exception.CannotCacheException;
import com.nextfin.exceptions.exception.Disabled2FAException;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.exceptions.exception.UserNotFoundException;
import com.nextfin.messaging.transaction.service.TransactionConfirmationService;
import com.nextfin.transaction.dto.*;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.enums.TransactionType;
import com.nextfin.transaction.repository.TransactionRepository;
import com.nextfin.transaction.scheduler.TransactionScheduler;
import com.nextfin.transaction.service.core.TransactionService;
import com.nextfin.transaction.service.processor.TransactionProcessor;
import com.nextfin.transaction.service.security.TransactionSecurityService;
import com.nextfin.transaction.service.validator.TransactionValidator;
import com.nextfin.users.entity.NextfinUserDetails;
import com.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CoreTransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProcessor transactionProcessor;

    @Lazy
    private final TransactionValidator transactionValidator;
    @Lazy
    private final TransactionScheduler transactionScheduler;
    private final Optional<TransactionSecurityService> securityService;

    private final AccountService accountService;
    private final TransactionConfirmationService confirmationService;

    private final CacheService cache;

    private final MessageSource messageSource;
    private final UserService userService;

    private static final int TRANSACTION_CACHE_SIZE = 20;
    private final TransactionMapper transactionMapper;

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
        Set<String> recentIds = fetchCacheRecents();
        return recentIds.stream().filter(id -> id.equals(transactionId.toString())).findAny().flatMap(
                id -> fetchFromCache(transactionId)).orElseGet(
                () -> transactionRepository.findById(transactionId).orElseThrow(
                        getNotFoundExceptionSupplier(transactionId)));
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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Transaction confirmTransaction(TransactionConfirmDto dto) {
        Transaction transaction = getTransaction(dto.transactionId());
        Account sourceAccount = transaction.getSourceAccount();
        String sourcePhone = sourceAccount.getHolder().getUser().getPreferredPhoneNumber();
        if (!check2fa(sourceAccount)) {
            throw new Disabled2FAException("Attempted transaction confirmation on disabled 2FA config.");
        }

        securityService.get().validateOTP(sourcePhone, dto.otp());
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
            cacheTransaction(userService.getCurrentUser().getId(), saved);
        } catch (UserNotFoundException e) {
            throw new CannotCacheException("User not found, cannot cache transaction " + transaction.getId());
        }
        return saved;
    }

    @NotNull
    private TransactionResponse finalizeTransaction(Transaction transaction, Transaction processedTransaction) {
        handleConfirmation(transaction.getSourceAccount(), processedTransaction);
        return new TransactionResultDto(processedTransaction, messageSource.getMessage("transaction.transfer.processed",
                                                                                       new UUID[]{transaction.getId()},
                                                                                       LocaleContextHolder.getLocale()));
    }


    private @NotNull Supplier<NotFoundException> getNotFoundExceptionSupplier(UUID transactionId) {
        return () -> new NotFoundException(messageSource.getMessage("transaction.notfound", new UUID[]{transactionId},
                                                                    LocaleContextHolder.getLocale()));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private TransactionResponse handleTransaction(Transaction transaction) {
        if (check2fa(transaction.getSourceAccount())) {
            securityService.get().sendOTP(transaction);
            return new TransactionResultDto(transaction, messageSource.getMessage(
                    "transaction.transfer" + ".awaiting" + "-validation", new UUID[]{transaction.getId()},
                    LocaleContextHolder.getLocale()));
        } else {
            return processTransaction(transaction);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private ScheduledTransactionDto handleScheduledTransaction(Transaction transaction) {
        if (check2fa(transaction.getSourceAccount())) {
            securityService.get().sendOTP(transaction);
            return ScheduledTransactionDto.builder().transaction(transaction).message(messageSource.getMessage(
                    "transaction.transfer.awaiting-validation",
                    new UUID[]{transaction.getId()},
                    LocaleContextHolder.getLocale())).build();
        }
        return scheduleTransaction(transaction);
    }

    private void handleConfirmation(Account sourceAccount, Transaction processedTransaction) {
        if (check2fa(sourceAccount)) {
            String initiator = sourceAccount.getHolder().getUser().getPreferredPhoneNumber();
            confirmationService.sendSMS(initiator, processedTransaction);
        }
    }

    public boolean isTransactionRelated(UUID transactionId) {
        Transaction transaction = getTransaction(transactionId);
        UUID currentUserId = ((NextfinUserDetails) SecurityContextHolder.getContext().getAuthentication()
                                                                        .getPrincipal()).getId();
        return (transaction.getSourceAccount().getHolder().getUser().getId().equals(
                currentUserId) || transaction.getTargetAccount().getHolder().getUser().getId()
                                                                .equals(currentUserId));
    }

    private boolean check2fa(Account sourceAccount) {
        return securityService.isPresent() && sourceAccount.getTransaction2FAEnabled();
    }

    private void cacheTransaction(UUID userId, Transaction transaction) {
        cache.addToSortedSet(userId.toString(),
                             transaction.getId().toString(),
                             transaction.getCreatedAt().toEpochMilli() / 1000.0);
        TransactionDetailsDto trnDetails = transactionMapper.toTransactionDetails(transaction);
        cache.setHashField(CacheUtils.buildTransactionsKey(userId),
                           CacheUtils.buildTransactionsHashKey(transaction.getId()), trnDetails);
    }

    private Set<String> fetchCacheRecents() {
        String setKey = CacheUtils.buildTransactionsKey(userService.getCurrentUser().getId());
        return cache.getFromSortedSet(setKey, 0, TRANSACTION_CACHE_SIZE);
    }

    private Optional<Transaction> fetchFromCache(UUID transactionId) {
        String hashKey = CacheUtils.buildTransactionsHashKey(transactionId);
        return cache.getAllFieldsFromHash(hashKey, Transaction.class);
    }
}
