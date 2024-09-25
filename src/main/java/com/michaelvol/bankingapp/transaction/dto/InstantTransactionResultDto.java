package com.michaelvol.bankingapp.transaction.dto;

import com.michaelvol.bankingapp.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InstantTransactionResultDto implements TransactionResultDto {
    Transaction transaction;
    String message;
}
