package com.michaelvol.bankingapp.account.repository;

import com.michaelvol.bankingapp.account.entity.Account;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @NotNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Account> S save(@NotNull S entity);
}
