package com.michaelvol.bankingapp.account.service;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.dto.DepositAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.GetAccountBalanceDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;

public interface AccountService {
    /**
     * Creates a new account given the info from {@link CreateAccountRequestDto}
     * @param createAccountRequestDto the dto
     * @return the new account creaeted
     */
    Account createAccount(CreateAccountRequestDto createAccountRequestDto);

    /**
     * Gets an account based on the accountId. If an account is not found / doesn't exist a {@link EntityNotFoundException} is thrown
     * @param accountId the accountId
     * @return the fetched account
     */
    Account getAccount(Long accountId);

    /**
     * Deposits an amount in the provided account based on accountId
     * @param accountId the accountId
     * @param dto       the dto containing the requested amount
     * @return the new updated balance of the account
     */
    BigDecimal depositAmount(Long accountId, DepositAmountRequestDto dto);

    /**
     * Withdraws an amount from the provided account based on accountId
     * @param accountId the account id
     * @param dto       the dto containing the amount to be withdrawn
     * @return the updated balance
     */
    BigDecimal withdrawAmount(Long accountId, WithdrawAmountRequestDto dto);

    /**
     * Gets the current balance of the account
     * @param accountId the account id
     * @return a dto containing the current balance and currency
     */
    GetAccountBalanceDto checkBalance(Long accountId);
}
