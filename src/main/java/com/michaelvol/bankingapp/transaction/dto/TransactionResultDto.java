package com.michaelvol.bankingapp.transaction.dto;

import com.michaelvol.bankingapp.transaction.entity.Transaction;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransactionResultDto {
    Transaction transaction;
    String message;
}
