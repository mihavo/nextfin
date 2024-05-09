package com.michaelvol.bankingapp.transaction.service.validator.impl;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.AccountService;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.service.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionValidatorImpl implements TransactionValidator {

    private final AccountService accountService;

    private final MessageSource messageSource;

    @Override
    public void validate(TransferRequestDto dto) {
        BigDecimal amount = dto.getAmount();
        Long sourceAccountId = dto.getSourceAccountId();
        Long targetAccountId = dto.getTargetAccountId();
        Currency currency = dto.getCurrency();

        List<Account> accounts = accountService.checkExistence(sourceAccountId, targetAccountId);
        Account sourceAccount = accounts.get(0);

        if (sourceAccountId.equals(targetAccountId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("transaction.transfer.account.same",
                                                                       null,
                                                                       LocaleContextHolder.getLocale()));
        }

        accountService.validateWithdrawal(sourceAccount, amount, currency);
    }
}
