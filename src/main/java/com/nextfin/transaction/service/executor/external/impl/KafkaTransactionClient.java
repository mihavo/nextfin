package com.nextfin.transaction.service.executor.external.impl;

import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.service.executor.external.AsyncTransactionClient;
import com.nextfin.transaction.service.executor.external.handlers.TransactionFailureHandler;
import com.nextfin.transaction.service.executor.external.handlers.TransactionSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTransactionClient implements AsyncTransactionClient {

    private final KafkaTemplate<String, Transaction> transactionTemplate;

    @Override
    public CompletableFuture<Transaction> submit(Transaction transaction) {
        CompletableFuture<Transaction> future = new CompletableFuture<>();
        log.debug("Submitting transaction {}  to external executor", transaction.getId());
        transactionTemplate.send("transactions", transaction).whenComplete((result, ex) -> {
            if (ex != null) future.completeExceptionally(ex);
            else log.debug("Transaction {} submitted with offset {}", transaction.getId(), result.getRecordMetadata().offset());
        });

        //Consume the response asynchronously
        //Wait in a separate thread for the listener to provide the result of the transaction
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(ex);
            }
        }).start();

        //Listen & Consume the result in the main thread
        new KafkaTransactionListenerService(future, latch);
        return future;
    }

    @Override
    public boolean cancel() {
        return false; //TODO: add cancellation flow
    }

    @Override
    public CompletableFuture<Transaction> submitWithHandlers(Transaction transaction,
                                                             TransactionSuccessHandler<Transaction> successHandler,
                                                             TransactionFailureHandler failureHandler) {
        return AsyncTransactionClient.super.submitWithHandlers(transaction, successHandler, failureHandler);
    }


}
