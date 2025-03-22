package com.nextfin.transaction.service.confirmation;

import com.nextfin.account.entity.Account;
import com.nextfin.transaction.entity.Transaction;

public interface ConfirmationService {
    /**
     * Handles confirmation of a processed transaction by providing a notification (SMS or other) to the user
     * that initiated the request
     *
     * @param sourceAccount
     * @param processedTransaction
     */
    void handleConfirmation(Account sourceAccount, Transaction processedTransaction);
}
