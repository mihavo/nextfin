package com.nextfin.transaction.service.executor.external.impl;

import com.nextfin.transaction.entity.Transaction;
import lombok.NoArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Service
@NoArgsConstructor
public class KafkaTransactionListenerService {

    private CompletableFuture<Transaction> futureResponse;
    private CountDownLatch latch;

    public KafkaTransactionListenerService(CompletableFuture<Transaction> futureResponse, CountDownLatch latch) {
        this.futureResponse = futureResponse;
        this.latch = latch;
    }

    @KafkaListener(topics = "transaction-results", groupId = "nextfin-executor-consumer-group")
    public void listen(Transaction transaction) {
        futureResponse.complete(transaction);
        latch.countDown();
    }

}
