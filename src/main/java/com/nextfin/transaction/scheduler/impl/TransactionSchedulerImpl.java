package com.nextfin.transaction.scheduler.impl;

import com.nextfin.exceptions.exception.BadRequestException;
import com.nextfin.transaction.dto.ScheduledTransactionDto;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.scheduler.TransactionScheduler;
import com.nextfin.transaction.service.core.TransactionService;
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
                transactionService.processScheduledTransaction(transaction)
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
