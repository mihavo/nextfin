package com.michaelvol.bankingapp.account.dto;

import java.util.Currency;

import com.michaelvol.bankingapp.holder.entity.Holder;
import org.antlr.v4.runtime.misc.NotNull;

public class AccountCreationDto {

    /**
     * Represents the id of the {@link Holder Account Holder}
     */
    @NotNull
    public Long holderId;


    public Currency currency;
}
