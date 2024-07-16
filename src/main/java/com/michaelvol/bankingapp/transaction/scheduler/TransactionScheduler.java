package com.michaelvol.bankingapp.transaction.scheduler;

import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;

public interface TransactionScheduler {

    void scheduleTransaction(Long accountId, TransferRequestDto transactionDetails);

    void scheduleTransaction(TransactionScheduleRequestDto request);
}
