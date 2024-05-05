package com.michaelvol.bankingapp.transaction.dto;

import com.michaelvol.bankingapp.transaction.entity.Transaction;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransferResultDto {
    Transaction transaction;
    String message;
}
