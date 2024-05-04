package com.michaelvol.bankingapp.account.dto;


import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.holder.entity.Holder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateAccountRequestDto {

    /**
     * Represents the id of the {@link Holder Account Holder}
     */
    @NotNull
    @Positive(message = "{account.id.positive}")
    public Long holderId;

    /**
     * Represents the id of the Manager
     */
    @NotNull
    @Positive(message = "{manager.id.positive}")
    public Long managerId;

    @Builder.Default
    @Size(min = 3, max = 3, message = "{account.currency.length")
    public String currencyCode = AppConstants.DEFAULT_CURRENCY_CODE;

    @Builder.Default
    public AccountType accountType = AccountType.SAVINGS;
}
