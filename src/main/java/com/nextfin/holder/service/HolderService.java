package com.nextfin.holder.service;

import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.holder.dto.CreateHolderDto;
import com.nextfin.holder.entity.Holder;
import com.nextfin.users.entity.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface HolderService {

    /**
     * Creates a new Holder entity and persists it to DB. Requires a User entity to be passed in order to associate the
     * holder with the user.
     * @param dto the {@link CreateHolderDto}
     * @param user the {@link User} entity
     * @return the created holder
     */
    Holder createHolder(CreateHolderDto dto, User user);

    /**
     * Fetches a holder from persistence from its id
     *
     * @param holderId the holder's id
     * @return the holder
     */
    Holder getHolderById(UUID holderId) throws NoSuchElementException;

    Holder getHolderByCurrentUser();

    /**
     * Fetches all accounts associated with the holder
     *
     * @param type the type of the accounts to filter by
     * @return the list of accounts found
     */
    List<Account> getAccounts(AccountType type);
}
