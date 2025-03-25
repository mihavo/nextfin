package com.nextfin.transaction.service.executor.external.handlers;

@FunctionalInterface
public interface TransactionSuccessHandler<T> {

    void onSuccess(T result);
}
