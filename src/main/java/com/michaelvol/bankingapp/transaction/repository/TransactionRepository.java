package com.michaelvol.bankingapp.transaction.repository;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findBySourceAccountOrTargetAccount(Account sourceAccount, Account targetAccount, Pageable pageable);

    Page<Transaction> findBySourceAccount(Account sourceAccount, Pageable pageable);

    Page<Transaction> findByTargetAccount(Account targetAccount, Pageable pageable);

    //    @Query(value = "SELECT sourceAccount FROM transactions where created_at > ")
    List<Transaction> getLast24HoursTransactions(Account account);
}
