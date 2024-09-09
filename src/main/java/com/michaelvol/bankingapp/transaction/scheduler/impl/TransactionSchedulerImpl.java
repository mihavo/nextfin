package com.michaelvol.bankingapp.transaction.scheduler.impl;

import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.scheduler.TransactionScheduler;
import com.michaelvol.bankingapp.transaction.service.core.TransactionService;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionSchedulerImpl implements TransactionScheduler {

    private final JobScheduler jobScheduler;
    private final TransactionService transactionService;

    @Override
    public void scheduleTransaction(TransactionScheduleRequestDto scheduleRequest) {
        jobScheduler.schedule(scheduleRequest.timestamp(), () -> {
            TransferRequestDto transferRequest = scheduleRequest.transactionDetails();
            transactionService.transferAmount(transferRequest);
        });
    }
}
