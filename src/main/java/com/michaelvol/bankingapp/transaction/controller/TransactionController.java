package com.michaelvol.bankingapp.transaction.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleResponseDto;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.scheduler.TransactionScheduler;
import com.michaelvol.bankingapp.transaction.service.core.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/transactions/")
@RequiredArgsConstructor
@Tag(name = "Transactions API", description = "Methods for transactions handling")
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionScheduler transactionScheduler;

    @PostMapping("/transfer")
    @Operation(summary = "Transfers amount from one account to another",
            description = "Transfers a specified amount provided that the source account has" +
                    " the required funds and the source and target accounts differ.")
    public ResponseEntity<TransferResultDto> transferAmount(@Valid @RequestBody TransferRequestDto dto) {
        TransferResultDto result = transactionService.transferAmount(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
    public ResponseEntity<TransactionScheduleResponseDto> scheduleTransaction(@RequestBody TransactionScheduleRequestDto dto) {

    }

}
