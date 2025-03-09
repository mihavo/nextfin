package com.nextfin.cache.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CacheUtils {

    public String buildTransactionsKey(Long accountId) {
        return "transactions:" + accountId;
    }

    public String buildTransactionsHashKey(UUID transactionId) {
        return "tnx:" + transactionId;
    }
}
