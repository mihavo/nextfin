package com.nextfin.account.repository;

import com.nextfin.account.dto.AccountSearchResultDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.holder.entity.Holder;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
                SELECT new com.nextfin.account.dto.AccountSearchResultDto(
                    a.id,a.iban, a.holder.firstName, a.holder.lastName, a.currency)
                FROM Account a
                WHERE (
                    LOWER(a.holder.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    LOWER(a.holder.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    LOWER(a.holder.user.email) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    LOWER(a.holder.user.username) LIKE LOWER(CONCAT('%', :query, '%')) OR
                    a.iban LIKE CONCAT('%', :query, '%')
                ) AND a.status = com.nextfin.account.enums.AccountStatus.ACTIVE
            """)
    Page<AccountSearchResultDto> searchAccounts(@Param("query") String query, Pageable pageable);
}
