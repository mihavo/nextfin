package com.michaelvol.bankingapp.account.service.validator.impl;

import com.michaelvol.bankingapp.account.dto.ValidateWithdrawalDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.validator.AccountValidator;
import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.service.core.TransactionService;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import javax.money.convert.MonetaryConversions;

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
        Long transactionLimit = account.getTransactionLimit();
        List<Transaction> latestTransactions = transactionService.getLatestSourceAccountTransactionsByDate(
                account,
                LocalDateTime.now().minusHours(24));
        BigDecimal totalAmount = latestTransactions.stream()
                                                   .map(Transaction::getAmount)
                                                   .reduce(BigDecimal.ZERO, BigDecimal::add).add(amount);
        if (totalAmount.compareTo(BigDecimal.valueOf(transactionLimit)) > 0) {
            throw new BadRequestException(messageSource.getMessage("account.transactionLimit.surpassed",
                                                                   new String[]{transactionLimit.toString(), account.getCurrency().getSymbol()},
                                                                   LocaleContextHolder.getLocale()));
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
}
