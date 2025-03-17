package com.nextfin.transaction.service.security;

import com.nextfin.exceptions.exception.ForbiddenException;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.service.core.TransactionService;
import com.nextfin.users.entity.NextfinUserDetails;
import com.nextfin.users.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionSecurityService {
    private final TransactionService transactionService;
    private final MessageSource messageSource;

    private static final Role[] TRANSACTION_VISIBILITY_ROLES = {Role.ADMIN};

    public boolean isTransactionRelatedToUser(UUID transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        return isTransactionRelatedToUser(transaction);
    }
    
    public void evaluatePermissions(Transaction transaction) throws ForbiddenException {
        boolean userOwnership = isTransactionRelatedToUser(transaction);
        boolean elevatedPermissions = hasElevatedPermissions();
        if (!userOwnership && !elevatedPermissions) {
            throw new ForbiddenException(
                    messageSource.getMessage("transaction.forbidden", null, LocaleContextHolder.getLocale()));
        }
    }

    private boolean hasElevatedPermissions() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(
                grantedAuthority -> Arrays.asList(TRANSACTION_VISIBILITY_ROLES).contains(
                        Role.valueOf(grantedAuthority.getAuthority())));
    }

    private boolean isTransactionRelatedToUser(Transaction transaction) {
        return currentUserIdEqualsSourceAccountId(transaction) || currentUserIdEqualsTargetAccountId(transaction);
    }

    private boolean currentUserIdEqualsSourceAccountId(Transaction transaction) {
        return (transaction.getSourceAccount().getHolder().getUser().getId().equals(
                ((NextfinUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
    }

    private boolean currentUserIdEqualsTargetAccountId(Transaction transaction) {
        return (transaction.getTargetAccount().getHolder().getUser().getId().equals(
                ((NextfinUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
    }
}

