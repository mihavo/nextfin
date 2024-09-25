package com.michaelvol.bankingapp.transaction.dto;

import com.michaelvol.bankingapp.transaction.entity.Transaction;

public interface TransactionResultDto {
    Transaction getTransaction();
    
    void setTransaction(Transaction transaction);

    String getMessage();

    void setMessage(String message);
}
