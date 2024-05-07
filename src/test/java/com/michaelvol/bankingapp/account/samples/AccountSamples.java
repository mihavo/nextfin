package com.michaelvol.bankingapp.account.samples;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.holder.entity.Holder;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Utility class to get sample account for testing purposes
 */
public class AccountSamples {

    public static Account sampleAccount(Long accountId, Holder holder, Employee manager) {
        return Account.builder()
                      .id(accountId)
                      .holder(holder)
                      .manager(manager)
                      .accountType(AccountType.SAVINGS)
                      .status(AccountStatus.ACTIVE)
                      .balance(BigDecimal.ZERO)
                      .currency(Currency.getInstance("EUR"))
                      .build();
    }
}
