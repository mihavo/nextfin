package com.nextfin.transaction.repository;

import com.nextfin.account.entity.Account;
import com.nextfin.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findBySourceAccountOrTargetAccount(Account sourceAccount, Account targetAccount, Pageable pageable);

    Page<Transaction> findBySourceAccount(Account sourceAccount, Pageable pageable);

    Page<Transaction> findByTargetAccount(Account targetAccount, Pageable pageable);

    @Query(value = "SELECT * FROM TRANSACTIONS T WHERE T.SOURCE_ACCOUNT_ID = :ACCOUNT_ID AND T.CREATED_AT >= :START_DATE", nativeQuery = true)
    List<Transaction> getTransactionsByAccountAndDate(@Param("ACCOUNT_ID") Long accountId, @Param("START_DATE") Instant instant);
}
