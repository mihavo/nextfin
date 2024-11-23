package com.michaelvol.bankingapp.transaction.scheduler;

import com.michaelvol.bankingapp.transaction.dto.ScheduledTransactionDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;

public interface TransactionScheduler {

    /**
     * Schedules a transaction to be processed at a later time
     * @param transaction the transaction to schedule
     * @return the result of the scheduling
     */
    ScheduledTransactionDto scheduleTransaction(Transaction transaction);
}
