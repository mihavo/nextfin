package com.nextfin.transaction.controller;

import com.nextfin.AppConstants;
import com.nextfin.transaction.dto.*;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.service.core.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/transactions/")
@RequiredArgsConstructor
@Tag(name = "Transactions API", description = "Methods for transactions handling")
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionMapper transactionMapper;

    private final MessageSource messageSource;

    @PostMapping("/initiate")
    @Operation(summary = "Initiates a transaction from a source account to a target account",
            description = "Transfers a specified amount provided that the source account has the required funds and " +
                    "the source and target accounts differ.")
    public ResponseEntity<TransactionResponseDto> transferAmount(@Valid @RequestBody TransferRequestDto dto) {
        TransactionResponse transactionResult = transactionService.initiateTransaction(dto);
        TransactionResponseDto response = transactionMapper.toTransactionResponse(transactionResult);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirms a transaction by providing the OTP sent to the source account")
    public ResponseEntity<TransactionResponseDto> confirmTransaction(@Valid @RequestBody TransactionConfirmDto dto) {
        Transaction transaction = transactionService.confirmTransaction(dto);
        TransactionResponse transactionResultDto = transactionService.processTransaction(transaction);
        TransactionResponseDto response = transactionMapper.toTransactionResponse(transactionResultDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Fetches a transaction by its ID")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        Transaction transaction = transactionService.getTransaction(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Checks the status of a Transaction")
    public ResponseEntity<TransactionStatus> checkStatus(@PathVariable UUID id) {
        TransactionStatus transactionStatus = transactionService.checkStatus(id);
        return new ResponseEntity<>(transactionStatus, HttpStatus.OK);
    }

    @PostMapping("/schedule")
    @Operation(summary = "Schedule a transaction to be processed at a later time")
    public ResponseEntity<TransactionResponseDto> scheduleTransaction(@RequestBody TransactionScheduleRequestDto dto) {
        TransactionResponse transactionResult = transactionService.initiateScheduledTransaction(dto);
        TransactionResponseDto response = transactionMapper.toTransactionResponse(transactionResult);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
