package com.michaelvol.nextfin.account.dto;


import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.michaelvol.nextfin.AppConstants;
import com.michaelvol.nextfin.account.enums.AccountType;
import com.michaelvol.nextfin.holder.entity.Holder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class CreateAccountRequestDto {

    /**
     * Represents the id of the {@link Holder Account Holder}
     */
    @NotNull
    public UUID holderId;

    /**
     * Represents the id of the Manager
     */
    @NotNull
    @Positive(message = "{manager.id.positive}")
    public Long managerId;

    @Builder.Default
    @Size(min = 3, max = 3, message = "{account.currency.length")
    @JsonSetter(nulls = Nulls.SKIP)
    public String currencyCode = AppConstants.DEFAULT_CURRENCY_CODE;

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    public AccountType accountType = AccountType.SAVINGS;
}
