package com.michaelvol.bankingapp.transaction.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransferResultDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.service.TransactionService;
import jakarta.validation.constraints.NotNull;
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
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResultDto> transferAmount(@NotNull @RequestBody TransferRequestDto dto) {
        TransferResultDto result = transactionService.transferAmount(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        Transaction transaction = transactionService.getTransaction(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<TransactionStatus> checkStatus(@PathVariable UUID id) {
        TransactionStatus transactionStatus = transactionService.checkStatus(id);
        return new ResponseEntity<>(transactionStatus, HttpStatus.OK);
    }

}
