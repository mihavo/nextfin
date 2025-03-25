package com.nextfin.transaction.service.executor.external.impl;

import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.service.executor.external.AsyncTransactionClient;
import com.nextfin.transaction.service.executor.external.handlers.TransactionFailureHandler;
import com.nextfin.transaction.service.executor.external.handlers.TransactionSuccessHandler;

import java.util.concurrent.CompletableFuture;

public class KafkaTransactionClient implements AsyncTransactionClient {
    @Override
    public CompletableFuture<Transaction> submit() {
        return null;
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public CompletableFuture<Transaction> submitWithHandlers(TransactionSuccessHandler<Transaction> successHandler,
                                                             TransactionFailureHandler failureHandler) {
        return AsyncTransactionClient.super.submitWithHandlers(successHandler, failureHandler);
    }
}
