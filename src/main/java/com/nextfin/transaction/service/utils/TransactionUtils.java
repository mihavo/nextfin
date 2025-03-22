package com.nextfin.transaction.service.utils;

import com.nextfin.account.entity.Account;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.transaction.service.security.MFATransactionService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionUtils {

    private final Optional<MFATransactionService> mfaTransactionService;
    private final MessageSource messageSource;

    public boolean check2fa(Account sourceAccount) {
        return mfaTransactionService.isPresent() && sourceAccount.getTransaction2FAEnabled();
    }


    public @NotNull Supplier<NotFoundException> getNotFoundExceptionSupplier(UUID transactionId) {
        return () -> new NotFoundException(
                messageSource.getMessage("transaction.notfound", new UUID[]{transactionId}, LocaleContextHolder.getLocale()));
    }

}
