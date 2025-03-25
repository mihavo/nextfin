package com.nextfin.transaction.service.executor.external;

import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.service.executor.external.handlers.TransactionFailureHandler;
import com.nextfin.transaction.service.executor.external.handlers.TransactionSuccessHandler;

import java.util.concurrent.CompletableFuture;


public interface AsyncTransactionClient {

    /**
     * Submits a transaction to an external service
     *
     * @return the result of the transaction obtained from the external service
     */
    CompletableFuture<Transaction> submit();

    /**
     * Cancel the transaction
     *
     * @return true if the cancellation succeeded
     */
    boolean cancel();

    /**
     * Submit the Transaction with success and failure handlers
     *
     * @param successHandler
     * @param failureHandler
     * @return the result on success, otherwise null
     */
    default CompletableFuture<Transaction> submitWithHandlers(TransactionSuccessHandler<Transaction> successHandler,
                                                              TransactionFailureHandler failureHandler) {
        return submit().thenApply(result -> {
            successHandler.onSuccess(result);
            return result;
        }).exceptionally(ex -> {
            failureHandler.onFailure(ex);
            return null;
        });
    }


}
