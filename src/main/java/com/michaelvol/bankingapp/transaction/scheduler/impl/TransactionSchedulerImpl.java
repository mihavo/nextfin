package com.michaelvol.bankingapp.transaction.scheduler.impl;

import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.scheduler.TransactionScheduler;
import com.michaelvol.bankingapp.transaction.service.core.TransactionService;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class TransactionSchedulerImpl implements TransactionScheduler {

    private final JobScheduler jobScheduler;
    private final TransactionService transactionService;

    @Override
    public TransactionScheduleResultDto scheduleTransaction(TransactionScheduleRequestDto scheduleRequest) {
        CompletableFuture<Transaction> transactionResult = new CompletableFuture<>();
        JobId jobId = jobScheduler.schedule(scheduleRequest.timestamp(), () -> {
            Transaction transaction = transactionService.initiateTransaction(scheduleRequest.transactionDetails());
            transactionResult.complete(transaction);
        });
        try {
            Transaction transaction = transactionResult.get();
            return new TransactionScheduleResultDto(transaction.getId(), jobId.asUUID(), scheduleRequest.timestamp());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
