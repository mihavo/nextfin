package com.nextfin.account.samples;

import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountStatus;
import com.nextfin.account.enums.AccountType;
import com.nextfin.employee.entity.Employee;
import com.nextfin.holder.entity.Holder;

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
