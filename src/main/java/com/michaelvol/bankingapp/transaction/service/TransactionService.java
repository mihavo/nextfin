package com.michaelvol.bankingapp.transaction.service;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.transaction.dto.GetTransactionOptions;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface TransactionService {

    /**
     * Transfers a specified amount from a source account to a target account in a given {@link java.util.Currency}.
     * When the same account is provided both as a source and target a BadRequestException is thrown
     * @param dto the dto containing the transfer details
     * @return a dto containing the transfer results
     */
    TransferResultDto transferAmount(TransferRequestDto dto);

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
     * Processes a transaction and updates its status
     * @param transactionId the transactionID
     * @return the updated (processed) transaction
     */
    Transaction processTransaction(UUID transactionId);

    /**
     * Gets a subset of transactions of a specified account based on a {@link PageRequest}
     * @param account the account that contains the transactions
     * @param options the {@link GetTransactionOptions}
     * @return a page containing transactions
     */
    Page<Transaction> getAccountTransactions(Account account, GetTransactionOptions options);
}
