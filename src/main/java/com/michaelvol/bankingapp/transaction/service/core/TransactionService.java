package com.michaelvol.bankingapp.transaction.service.core;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.transaction.dto.*;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    /**
     * Transfers a specified amount from a source account to a target account in a given {@link java.util.Currency}.
     * When the same account is provided both as a source and target a BadRequestException is thrown
     * @param dto the dto containing the transfer details
     * @return a dto containing the transfer results
     */
    TransactionResultDto initiateTransaction(TransferRequestDto dto);

    /**
     * Initiates a scheduled transaction and follows the same rules as {@link #initiateTransaction(TransferRequestDto)}
     * @param dto the dto containing the transfer details
     * @return a dto containing the scheduled transfer results
     */
    TransactionResultDto initiateScheduledTransaction(TransactionScheduleRequestDto dto);
    /**
     * Fetches a transaction
     * @param transactionId the transactionID
     * @return the {@link Transaction}
     */
    Transaction getTransaction(UUID transactionId);

    /**
     * Gets the {@link TransactionStatus} of a transaction by its id
     * @param transactionId the transactionID
     * @return the status
     */
    TransactionStatus checkStatus(UUID transactionId);

    /**
     * Schedules a transaction and updates its status
     *
     * @param transaction the transaction to schedule
     * @return the scheduled transaction
     */
    ScheduledTransactionDto scheduleTransaction(Transaction transaction);

    /**
     * Processes a transaction and updates its status
     * @param transaction the transaction to process
     * @return the updated (processed) transaction
     */
    TransactionResultDto processTransaction(Transaction transaction);

    /**
     * Processes a scheduled transaction and updates its status
     *
     * @param transaction the transaction to process
     * @return the updated (processed) transaction
     */
    TransactionResultDto processScheduledTransaction(Transaction transaction);


    /**
     * Gets a subset of transactions of a specified account based on a {@link PageRequest}
     * @param account the account that contains the transactions
     * @param options the {@link GetTransactionOptions}
     * @return a page containing transactions
     */
    Page<Transaction> getAccountTransactions(Account account, GetTransactionOptions options);

    /**
     * Gets the latest transactions that have been initiated (source transactions) from a specified account beginning from a given datetime
     * @param sourceAccount the source account
     * @param instant the earliest timestamp of the transactions
     * @return the list of found transactions
     */
    List<Transaction> getLatestSourceAccountTransactionsByDate(Account sourceAccount, Instant instant);

    /**
     * Confirms a transaction by validating the OTP sent to the source account.
     * @param dto the {@link TransactionConfirmDto} containing the transactionID and the OTP
     * @return the confirmed transaction
     */
    Transaction confirmTransaction(@Valid TransactionConfirmDto dto);
}
