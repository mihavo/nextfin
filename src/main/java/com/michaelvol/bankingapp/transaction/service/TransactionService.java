package com.michaelvol.bankingapp.transaction.service;

import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;

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
}
