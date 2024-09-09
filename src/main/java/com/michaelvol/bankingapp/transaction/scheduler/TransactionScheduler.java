package com.michaelvol.bankingapp.transaction.scheduler;

import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;

public interface TransactionScheduler {

    void scheduleTransaction(TransactionScheduleRequestDto request);
}
