package com.michaelvol.bankingapp.transaction.service.processor;

import com.michaelvol.bankingapp.transaction.entity.Transaction;

public interface TransactionProcessor {
    /**
     * Facilitates the actual transaction. Any given transaction has already been checked for
     * potential validation errors.
     * @param transaction - the transaction to be processed
     */
    public void doTransaction(Transaction transaction);
}
