package com.michaelvol.bankingapp.transaction.scheduler.impl;

import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.scheduler.TransactionScheduler;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionSchedulerImpl implements TransactionScheduler {

    private final JobScheduler jobScheduler;

    @Override
    public void scheduleTransaction(Long accountId, TransferRequestDto transactionDetails) {

    }

    @Override
    public void scheduleTransaction(TransactionScheduleRequestDto request) {
        scheduleTransaction(request.accountId(), request.transactionDetails());
    }
}
