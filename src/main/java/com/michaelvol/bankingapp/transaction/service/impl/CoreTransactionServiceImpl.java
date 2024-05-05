package com.michaelvol.bankingapp.transaction.service.impl;

import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.service.TransactionService;

import java.util.UUID;

public class CoreTransactionServiceImpl implements TransactionService {

    @Override
    public TransferResultDto transferAmount(TransferRequestDto dto) {
        return null;
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
