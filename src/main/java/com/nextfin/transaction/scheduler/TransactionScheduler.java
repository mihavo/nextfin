package com.nextfin.transaction.scheduler;

import com.nextfin.transaction.dto.ScheduledTransactionDto;
import com.nextfin.transaction.entity.Transaction;

public interface TransactionScheduler {

    /**
     * Schedules a transaction to be processed at a later time
     * @param transaction the transaction to schedule
     * @return the result of the scheduling
     */
    ScheduledTransactionDto scheduleTransaction(Transaction transaction);
}
