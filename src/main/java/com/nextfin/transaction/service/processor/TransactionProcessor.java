package com.nextfin.transaction.service.processor;

import com.nextfin.transaction.entity.Transaction;

public interface TransactionProcessor {
    /**
     * Facilitates the actual transaction. Any given transaction has already been checked for
     * potential validation errors.
     * @param transaction - the transaction
     */
    void doTransaction(Transaction transaction);

    /**
     * Executes the whole flow of the transaction: storing it to db, doing the transaction and updating the status
     * @param transaction the transaction to be processed
     * @return the updated Transaction from persistence
     */
    Transaction process(Transaction transaction);
}
