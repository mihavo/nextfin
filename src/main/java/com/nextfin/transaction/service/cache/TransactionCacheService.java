package com.nextfin.transaction.service.cache;

import com.nextfin.cache.CacheService;
import com.nextfin.cache.utils.CacheUtils;
import com.nextfin.transaction.dto.TransactionDetailsDto;
import com.nextfin.transaction.dto.TransactionMapper;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransactionCacheService {

    private final CacheService cache;
    private final TransactionMapper transactionMapper;
    private final UserService userService;

    private static final int TRANSACTION_CACHE_SIZE = 20;

    public void cacheTransaction(UUID userId, Transaction transaction) {
        String setKey = CacheUtils.buildTransactionsSetKey(userId);
        cache.addToSortedSet(setKey, transaction.getId().toString(),
                             transaction.getCreatedAt().toEpochMilli() / 1000.0);
        TransactionDetailsDto trnDetails = transactionMapper.toTransactionDetails(transaction);
        cache.setHashObject(CacheUtils.buildTransactionHashKey(transaction.getId()), trnDetails);
    }

    public Set<String> fetchCacheRecents() {
        String setKey = CacheUtils.buildTransactionsSetKey(userService.getCurrentUser().getId());
        return cache.getFromSortedSet(setKey, 1, TRANSACTION_CACHE_SIZE);
    }

    public Optional<Transaction> fetchFromCache(UUID transactionId) {
        String hashKey = CacheUtils.buildTransactionHashKey(transactionId);
        return cache.getAllFieldsFromHash(hashKey, TransactionDetailsDto.class).map(transactionMapper::toTransaction);
    }
}
