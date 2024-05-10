package com.michaelvol.bankingapp.account.service.validator;

import com.michaelvol.bankingapp.account.dto.ValidateWithdrawalDto;

public interface AccountValidator {
    /**
     * Validates an amount in the given currency for being able to be withdrawn i.e. funds suffice
     * @param dto the dto containing info about the withdrawal
     */
    void validateWithdrawal(ValidateWithdrawalDto dto);
}
