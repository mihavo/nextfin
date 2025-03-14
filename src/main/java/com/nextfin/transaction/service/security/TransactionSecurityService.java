package com.nextfin.transaction.service.security;

import com.nextfin.exceptions.exception.ForbiddenException;
import com.nextfin.transaction.dto.TransactionMapper;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.repository.TransactionRepository;
import com.nextfin.transaction.service.core.TransactionService;
import com.nextfin.users.entity.NextfinUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionSecurityService {
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;


    public boolean isTransactionRelated(UUID transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        UUID currentUserId =
                ((NextfinUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return (transaction.getSourceAccount().getHolder().getUser().getId().equals(
                currentUserId) || transaction.getTargetAccount().getHolder().getUser().getId().equals(currentUserId));
    }

    public void evaluatePermissions(Transaction transaction) throws ForbiddenException {
        }
    }
}

