package com.michaelvol.bankingapp.transaction.service.processor;

import com.michaelvol.bankingapp.transaction.entity.Transaction;

public interface TransactionProcessor {
    /**
     * Facilitates the actual transaction. Any given transaction has already been checked for
     * potential validation errors.
     * @param transaction - the transaction
     */
    public void doTransaction(Transaction transaction);

    /**
     * Executes the whole flow of the transaction: storing it to db, doing the transaction and updating the status
     * @param transaction the transaction to be processed
     * @return the updated Transaction from persistence
     */
    public Transaction process(Transaction transaction);
}
