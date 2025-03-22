package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transaction.updatedAt", target = "timestamp")
    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "transaction.sourceAccount.id", target = "sourceAccountId")
    @Mapping(source = "transaction.targetAccount.id", target = "targetAccountId")
    @Mapping(source = "transaction.currency", target = "currency")
    @Mapping(source = "transaction.transactionStatus", target = "status")
    TransactionResponseDto toTransactionResponse(TransactionResponse response);

    @Mapping(source = "sourceAccount.id", target = "sourceAccountId")
    @Mapping(source = "targetAccount.id", target = "targetAccountId")
    @Mapping(source = "sourceAccount.holder.user.id", target = "sourceUserId")
    @Mapping(source = "targetAccount.holder.user.id", target = "targetUserId")
    @Mapping(source = "transactionStatus", target = "status")
    @Mapping(source = "transactionType", target = "type")
    TransactionDetailsDto toTransactionDetails(Transaction transaction);

    @Mapping(source = "status", target = "transactionStatus")
    @Mapping(source = "type", target = "transactionType")
    @Mapping(source = "sourceAccountId", target = "sourceAccount.id")
    @Mapping(source = "targetAccountId", target = "targetAccount.id")
    @Mapping(source = "sourceUserId", target = "sourceAccount.holder.user.id")
    @Mapping(source = "targetUserId", target = "targetAccount.holder.user.id")
    Transaction toTransaction(TransactionDetailsDto transactionDetails);
}
