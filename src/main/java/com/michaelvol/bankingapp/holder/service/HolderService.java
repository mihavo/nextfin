package com.michaelvol.bankingapp.holder.service;

import com.michaelvol.bankingapp.holder.dto.CreateHolderRequestDto;
import com.michaelvol.bankingapp.holder.entity.Holder;

import java.util.NoSuchElementException;

public interface HolderService {

    /**
     * Creates a new Holder entity and persists it to DB
     *
     * @param dto the {@link CreateHolderRequestDto}
     * @return the created holder
     */
    Holder createHolder(CreateHolderRequestDto dto);

    /**
     * Fetches a holder from persistence from its id
     *
     * @param holderId the holder's id
     * @return the holder
     */
    Holder getHolderById(Long holderId) throws NoSuchElementException;
}
