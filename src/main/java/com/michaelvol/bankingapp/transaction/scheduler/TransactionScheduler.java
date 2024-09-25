package com.michaelvol.bankingapp.transaction.scheduler;

import com.michaelvol.bankingapp.transaction.dto.ScheduledTransactionResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;

public interface TransactionScheduler {

    /**
     * Schedules a transaction to be processed at a later time
     * @param transaction the transaction to schedule
     * @return the result of the scheduling
     */
    ScheduledTransactionResultDto scheduleTransaction(Transaction transaction);
}
