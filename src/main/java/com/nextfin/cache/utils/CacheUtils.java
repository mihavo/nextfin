package com.nextfin.cache.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CacheUtils {

    public String buildTransactionsKey(UUID userId) {
        return "transactions:user:" + userId;
    }

    public String buildTransactionsHashKey(UUID transactionId) {
        return "tnx:" + transactionId;
    }
}
