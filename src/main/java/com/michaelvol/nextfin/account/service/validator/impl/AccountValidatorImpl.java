package com.michaelvol.nextfin.account.service.validator.impl;

import com.michaelvol.nextfin.account.dto.ValidateWithdrawalDto;
import com.michaelvol.nextfin.account.entity.Account;
import com.michaelvol.nextfin.account.service.validator.AccountValidator;
import com.michaelvol.nextfin.exceptions.exception.BadRequestException;
import com.michaelvol.nextfin.exceptions.exception.ForbiddenException;
import com.michaelvol.nextfin.transaction.entity.Transaction;
import com.michaelvol.nextfin.transaction.service.core.TransactionService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Currency;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountValidatorImpl implements AccountValidator {
    private final TransactionService transactionService;
    private final MessageSource messageSource;

    @Override
    public void validateWithdrawal(ValidateWithdrawalDto dto) {
        Account account = dto.getAccount();
        BigDecimal amount = dto.getAmount();
        Currency currency = dto.getCurrency();
        //Check if current account has not exceeded daily transaction limit
        if (account.getTransactionLimitEnabled()) {
            Long transactionLimit = account.getTransactionLimit();
            List<Transaction> latestTransactions = transactionService.getLatestSourceAccountTransactionsByDate(
                    account,
                    LocalDateTime.now().minusHours(24).atZone(ZoneId.systemDefault()).toInstant());

            BigDecimal totalAmount = latestTransactions.stream()
                                                       .map(Transaction::getAmount)
                                                       .reduce(BigDecimal.ZERO, BigDecimal::add).add(amount);
            if (totalAmount.compareTo(BigDecimal.valueOf(transactionLimit)) > 0) {
                throw new BadRequestException(messageSource.getMessage("account.transaction-limit.surpassed",
                                                                       new String[]{transactionLimit.toString(), account.getCurrency().getSymbol()},
                                                                       LocaleContextHolder.getLocale()));
            }
        }

        //Convert amount to account's selected currency
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
