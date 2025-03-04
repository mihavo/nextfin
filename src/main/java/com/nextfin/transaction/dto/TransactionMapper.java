package com.nextfin.transaction.dto;

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
}
