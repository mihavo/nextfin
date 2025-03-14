package com.nextfin.cache.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CacheUtils {

    public String buildTransactionsSetKey(UUID userId) {
        return "transactions:user:" + userId;
    }

    public String buildTransactionHashKey(UUID transactionId) {
        return "tnx:" + transactionId;
    }
}
