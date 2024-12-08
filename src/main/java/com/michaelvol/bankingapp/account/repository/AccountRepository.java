package com.michaelvol.bankingapp.account.repository;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.holder.entity.Holder;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @NotNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Account> S save(@NotNull S entity);

    List<Account> findAllByHolder(Holder holder);

    List<Account> findAllByHolderAndAccountType(Holder holder, AccountType accountType);
}
