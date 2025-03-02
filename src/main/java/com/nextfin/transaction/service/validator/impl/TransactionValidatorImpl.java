package com.nextfin.transaction.service.validator.impl;

import com.nextfin.account.dto.ValidateWithdrawalDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.account.service.validator.AccountValidator;
import com.nextfin.transaction.dto.TransferRequestDto;
import com.nextfin.transaction.service.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class TransactionValidatorImpl implements TransactionValidator {

    private final AccountService accountService;
    private final AccountValidator accountValidator;

    private final MessageSource messageSource;

    @Override
    public void validate(TransferRequestDto dto) {
        BigDecimal amount = dto.getAmount();

        Long sourceAccountId = dto.getSourceAccountId();
        Long targetAccountId = dto.getTargetAccountId();
        Currency currency = dto.getCurrency();

        Account sourceAccount = accountService.getAccount(sourceAccountId);
        accountValidator.validateAccountOwnership(sourceAccount);
        accountService.checkExistence(targetAccountId);

        if (sourceAccountId.equals(targetAccountId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("transaction.transfer.account.same",
                                                                       null,
                                                                       LocaleContextHolder.getLocale()));
        }

        accountService.validateWithdrawal(ValidateWithdrawalDto.builder()
                                                               .account(sourceAccount)
                                                               .amount(amount)
                                                               .currency(currency)
                                                               .build());
    }
}
