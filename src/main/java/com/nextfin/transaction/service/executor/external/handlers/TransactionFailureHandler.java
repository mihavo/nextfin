package com.nextfin.transaction.service.executor.external.handlers;

@FunctionalInterface
public interface TransactionFailureHandler {

    void onFailure(Throwable throwable);
}
