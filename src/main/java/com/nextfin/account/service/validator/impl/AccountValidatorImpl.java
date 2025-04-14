package com.nextfin.account.service.validator.impl;

import com.nextfin.account.dto.ValidateWithdrawalDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.service.validator.AccountValidator;
import com.nextfin.cache.CacheService;
import com.nextfin.cache.utils.CacheUtils;
import com.nextfin.currency.CurrencyConverterService;
import com.nextfin.exceptions.exception.BadRequestException;
import com.nextfin.exceptions.exception.ForbiddenException;
import com.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountValidatorImpl implements AccountValidator {
    private final CurrencyConverterService currencyConverterService;
    private final MessageSource messageSource;
    private final UserService userService;
    private final CacheService cache;

    private static final int ACCOUNTS_CACHE_SIZE = 20;


    @Override
    public void validateWithdrawal(ValidateWithdrawalDto dto) {
        Account account = dto.getAccount();
        validateDailyTotals(account, dto.getAmount());
        validateSufficientFunds(dto, account);
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

    //Validates that the requested amount can be withdrawn from the account's balance
    private void validateSufficientFunds(ValidateWithdrawalDto dto, Account account) {
        BigDecimal amount = dto.getAmount();
        Currency currency = dto.getCurrency();
        Currency targetCurrency = account.getCurrency();
        BigDecimal convertedAmount = currencyConverterService.convert(amount, currency, targetCurrency);
        if (account.getBalance().compareTo(convertedAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("account.withdraw.insufficient", null,
                                                                       LocaleContextHolder.getLocale()));
        }
    }
    // Calculates the new daily total after this transaction
    private BigDecimal calculateNewDailyTotal(Account account, BigDecimal amount) {
        return account.getDailyTotal().add(amount);
    }


    @Override
    public void validateAccountOwnership(Account account) {
        boolean isValidated;
        if (account == null)
            throw new BadRequestException(messageSource.getMessage("account.notfound", null, LocaleContextHolder.getLocale()));

        Set<String> accounts = getCurrentAccounts();
        if (accounts == null || accounts.isEmpty()) // Fetch from Cache
            isValidated = account.getHolder().getUser().equals(userService.getCurrentUser());
        else isValidated = accounts.contains(account.getId().toString()); //Fetch from DB via lazy-loaded relations

        if (!isValidated)
            throw new ForbiddenException(messageSource.getMessage("account.forbidden", null, LocaleContextHolder.getLocale()));
    }

    private Set<String> getCurrentAccounts() {
        String key = CacheUtils.buildAccountsSetKey(userService.getCurrentUser().getId());
        return cache.getFromSortedSet(key, 1, ACCOUNTS_CACHE_SIZE);
    }
}
