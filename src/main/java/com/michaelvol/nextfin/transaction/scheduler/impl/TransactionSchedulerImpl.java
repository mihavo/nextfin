package com.michaelvol.nextfin.transaction.scheduler.impl;

import com.michaelvol.nextfin.exceptions.exception.BadRequestException;
import com.michaelvol.nextfin.transaction.dto.ScheduledTransactionDto;
import com.michaelvol.nextfin.transaction.entity.Transaction;
import com.michaelvol.nextfin.transaction.scheduler.TransactionScheduler;
import com.michaelvol.nextfin.transaction.service.core.TransactionService;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionSchedulerImpl implements TransactionScheduler {

    private final JobScheduler jobScheduler;
    private final TransactionService transactionService;

    private final MessageSource messageSource;

    @Override
    public ScheduledTransactionDto scheduleTransaction(Transaction transaction) {
        LocalDateTime scheduledAt = transaction.getScheduledAt();
        if (scheduledAt == null || scheduledAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Transaction must have a scheduled time");
        }
        JobId jobId = jobScheduler.schedule(scheduledAt, () ->
                transactionService.processTransaction(transaction)
        );

        String message = messageSource.getMessage(
                "transaction.transfer.scheduled",
                null,
                LocaleContextHolder.getLocale());
        return new ScheduledTransactionDto(transaction,
                                                 jobId.asUUID(),
                                                 transaction.getScheduledAt(),
                                                 message);
    }
}
