package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.Transaction;

public interface TransactionResponse {
    Transaction getTransaction();

    String getMessage();
}
