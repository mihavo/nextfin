package com.michaelvol.bankingapp.transaction.service;

import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;

public interface TransactionService {

    /**
     * Transfers a specified amount from a source account to a target account in a given {@link java.util.Currency}.
     * When the same account is provided both as a source and target a BadRequestException is thrown
     * @param dto the dto containing the transfer details
     * @return a dto containing the transfer results
     */
    TransferResultDto transferAmount(TransferRequestDto dto);
}
