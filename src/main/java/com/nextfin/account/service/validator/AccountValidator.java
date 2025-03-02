package com.nextfin.account.service.validator;

import com.nextfin.account.dto.ValidateWithdrawalDto;
import com.nextfin.account.entity.Account;
import com.nextfin.exceptions.exception.ForbiddenException;
import com.nextfin.exceptions.exception.NotFoundException;

public interface AccountValidator {
    /**
     * Validates an amount in the given currency for being able to be withdrawn i.e. funds suffice
     * @param dto the dto containing info about the withdrawal
     */
    void validateWithdrawal(ValidateWithdrawalDto dto);


    /**
     * Validates that the provided account belongs to the currently authenticated user
     *
     * @param account the account to validate
     */
    void validateAccountOwnership(Account account) throws NotFoundException, ForbiddenException;
}
