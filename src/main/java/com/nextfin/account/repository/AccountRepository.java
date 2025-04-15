package com.nextfin.account.repository;

import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.holder.entity.Holder;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @NotNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Account> S save(@NotNull S entity);

    List<Account> findAllByHolder(Holder holder);

    List<Account> findAllByHolderAndAccountType(Holder holder, AccountType accountType);

    @Query("SELECT us.id FROM User us JOIN Holder ho ON ho.user = us JOIN ho.accounts acc WHERE acc.id = :id")
    UUID findUserIdByAccountId(Long id);

    @Modifying
    @Query("UPDATE Account a SET a.dailyTotal = 0")
    void resetDailyTotals();

    @Query("SELECT a FROM Account a WHERE a.holder.user.id = :userId")
    List<Account> getCurrentUserAccounts(UUID userId);
}
