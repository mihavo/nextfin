package com.michaelvol.bankingapp.transaction.scheduler;

import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleResultDto;

public interface TransactionScheduler {

    TransactionScheduleResultDto scheduleTransaction(TransactionScheduleRequestDto request);
}
