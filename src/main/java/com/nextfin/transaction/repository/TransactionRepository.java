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

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount IN :accounts")
    Page<Transaction> findAllBySourceAccounts(List<Account> accounts, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.targetAccount IN :accounts")
    Page<Transaction> findAllByTargetAccounts(List<Account> accounts, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.targetAccount IN :accounts OR t.sourceAccount IN :accounts")
    Page<Transaction> findAllBySourceOrTargetAccounts(List<Account> accounts, Pageable pageable);

    @Query("""
                SELECT t FROM Transaction t
                WHERE t.category = com.nextfin.transaction.entity.TransactionCategory.TRANSFERS
                  AND t.createdAt BETWEEN :start AND :end
                  AND t.targetAccount.holder.id = :holderId
            """)
    List<Transaction> findIncomeByTimePeriod(@Param("holderId") UUID holderId, @Param("start") Instant start,
                                             @Param("end") Instant end);

    @Query("""
                SELECT t FROM Transaction t
                  WHERE t.createdAt BETWEEN :start AND :end
                  AND t.sourceAccount.holder.id = :holderId
            """)
    List<Transaction> findExpensesByTimePeriod(@Param("holderId") UUID holderId, @Param("start") Instant start,
                                               @Param("end") Instant end);
}
