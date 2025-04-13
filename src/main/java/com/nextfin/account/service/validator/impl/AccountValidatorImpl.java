package com.nextfin.account.service.validator.impl;

import com.nextfin.account.dto.ValidateWithdrawalDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.service.validator.AccountValidator;
import com.nextfin.exceptions.exception.BadRequestException;
import com.nextfin.exceptions.exception.ForbiddenException;
import com.nextfin.transaction.service.core.TransactionService;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class AccountValidatorImpl implements AccountValidator {
    private final TransactionService transactionService;
    private final MessageSource messageSource;

    @Override
    public void validateWithdrawal(ValidateWithdrawalDto dto) {
        Account account = dto.getAccount();
        validateDailyTotals(account, dto.getAmount());

        //Convert amount to account's selected currency
        BigDecimal amount = dto.getAmount();
        Currency currency = dto.getCurrency();
        Currency targetCurrency = account.getCurrency();
        MonetaryConversions.getConversion(targetCurrency.getCurrencyCode());
        BigDecimal convertedAmount = Money.of(amount, currency.getCurrencyCode())
                                          .getNumber()
                                          .numberValue(BigDecimal.class);
        if (account.getBalance().compareTo(convertedAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("account.withdraw.insufficient", null,
                                                                       LocaleContextHolder.getLocale()));
        }
    }

    //Check if current account has not exceeded daily transaction limit
    private void validateDailyTotals(Account account, BigDecimal amount) {
        if (account.getTransactionLimitEnabled() && calculateNewDailyTotal(account, amount).compareTo(
                BigDecimal.valueOf(account.getTransactionLimit())) > 0) {
            throw new BadRequestException(messageSource.getMessage("account.transaction-limit.surpassed",
                                                                   new String[]{account.getTransactionLimit().toString(),
                                                                                account.getCurrency().getSymbol()},
                                                                   LocaleContextHolder.getLocale()));
        }
    }

    // Calculates the new daily total after this transaction
    private BigDecimal calculateNewDailyTotal(Account account, BigDecimal amount) {
        return account.getDailyTotal().add(amount);
    }


    @Override
    public void validateAccountOwnership(Account account) {
        if (account == null) {
            throw new BadRequestException(messageSource.getMessage("account.notfound", null,
                    LocaleContextHolder.getLocale()));
        }
        UserDetails authenticatedUserDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (!account.getHolder().getUser().equals(authenticatedUserDetails)) {
            throw new ForbiddenException(
                    messageSource.getMessage("account.forbidden", null,
                            LocaleContextHolder.getLocale()));
        }
    }
}
