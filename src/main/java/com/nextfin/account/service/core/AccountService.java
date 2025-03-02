package com.nextfin.account.service.core;

import com.michaelvol.nextfin.account.dto.*;
import com.nextfin.account.dto.*;
import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.account.service.validator.AccountValidator;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.users.entity.User;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    /**
     * Checks that all the provided account ids correspond to existing accounts. If any accountId
     * is not matched an {@link EntityNotFoundException} is thrown
     * @param accountIds a series of account ids
     */
    void checkExistence(Long... accountIds) throws EntityNotFoundException;

    /**
     * Creates a new account given the info from {@link CreateAccountRequestDto}
     * @param createAccountRequestDto the dto
     * @return the new account created
     */
    Account createAccount(CreateAccountRequestDto createAccountRequestDto);

    /**
     * Gets an account based on the accountId. If an account is not found / doesn't exist a {@link EntityNotFoundException} is thrown
     * @param accountId the accountId
     * @return the fetched account
     */
    Account getAccount(Long accountId) throws NotFoundException;

    /**
     * Gets a set of accounts based on the provided accountIds. If an account is not found / doesn't exist a {@link EntityNotFoundException} is thrown
     * @param accountIds the accountIds
     * @return a list of accounts
     */
    List<Account> getAccounts(Long... accountIds) throws EntityNotFoundException;

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

    /**
     * Uses an {@link AccountValidator} to validate a withdrawal
     * @param dto the dto containing the withdrawal info
     */
    void validateWithdrawal(ValidateWithdrawalDto dto);

    /**
     * Updates the transaction limit of the account.
     * @param account          the account to update
     * @param transactionLimit the new transaction limit
     */
    void updateTransactionLimit(Account account, Long transactionLimit);

    /**
     * Toggles the transaction limit of the account
     * @param account the account to toggle
     * @return the new state of the transaction limit
     */
    Boolean toggleTransactionLimit(Account account);

    /**
     * Toggles 2FA verification for transactions
     * @param account the account to toggle 2FA
     * @return the new state of the transaction 2FA
     */
    Boolean toggleTransaction2FA(Account account);

    /**
     * Toggles SMS confirmation for transactions
     *
     * @param account the account to toggle SMS confirmation
     * @return the new state of the transaction SMS confirmation
     */
    Boolean toggleTransactionSMSConfirmation(Account account);

    List<Account> getAccountsByUser(User owner, AccountType type);
}
