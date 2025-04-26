package com.nextfin.transaction.dto;

import com.nextfin.transaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transaction.updatedAt", target = "timestamp")
    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "transaction.sourceAccountId", target = "sourceAccountId")
    @Mapping(source = "transaction.targetAccountId", target = "targetAccountId")
    @Mapping(source = "transaction.currency", target = "currency")
    @Mapping(source = "transaction.status", target = "status")
    @Mapping(source = "transaction.category", target = "category")
    TransactionResponseDto toTransactionResponse(TransactionResponse response);

    @Mapping(source = "sourceAccountId", target = "sourceAccountId")
    @Mapping(source = "targetAccountId", target = "targetAccountId")
    @Mapping(source = "sourceAccount.holder.user.id", target = "sourceUserId")
    @Mapping(source = "targetAccount.holder.user.id", target = "targetUserId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "category", target = "category")
    @Mapping(target = "targetName", expression = "java(transaction.getTargetName())")
    TransactionDetailsDto toTransactionDetails(Transaction transaction);

    @Mapping(source = "status", target = "status")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "sourceAccountId", target = "sourceAccountId")
    @Mapping(source = "targetAccountId", target = "targetAccountId")
    @Mapping(source = "sourceUserId", target = "sourceAccount.holder.user.id")
    @Mapping(source = "targetUserId", target = "targetAccount.holder.user.id")
    Transaction toTransaction(TransactionDetailsDto transactionDetails);
}
