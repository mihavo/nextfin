package com.michaelvol.bankingapp.transaction.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.transaction.dto.TransactionConfirmDto;
import com.michaelvol.bankingapp.transaction.dto.TransactionResultDto;
import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleRequestDto;
import com.michaelvol.bankingapp.transaction.dto.TransactionScheduleResponseDto;
import com.michaelvol.bankingapp.transaction.dto.TransferRequestDto;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.scheduler.TransactionScheduler;
import com.michaelvol.bankingapp.transaction.service.core.TransactionService;
import com.michaelvol.bankingapp.transaction.service.security.TransactionSecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    private final TransactionSecurityService securityService;

    private final TransactionScheduler transactionScheduler;

    private final MessageSource messageSource;

    @PostMapping("/initiate")
    @Operation(summary = "Initiates a transaction from a source account to a target account",
            description = "Transfers a specified amount provided that the source account has" +
                    " the required funds and the source and target accounts differ. Sends OTP for validating the source.")
    public ResponseEntity<TransactionResultDto> transferAmount(@Valid @RequestBody TransferRequestDto dto) {
        Transaction transaction = transactionService.initiateTransaction(dto);
        TransactionResultDto resultDto = TransactionResultDto.builder()
                                                             .transaction(transaction)
                                                             .message(messageSource.getMessage(
                                                                     "transaction.transfer.awaiting-validation",
                                                                     new UUID[]{transaction.getId()},
                                                                     LocaleContextHolder.getLocale()))
                                                             .build();
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirms a transaction by providing the OTP sent to the source account")
    public ResponseEntity<TransactionResultDto> confirmTransaction(@Valid @RequestBody TransactionConfirmDto dto) {
        Transaction transaction = transactionService.getTransaction(dto.transactionId());
        String sourcePhone = transaction.getSourceAccount().getHolder().getUser().getPreferredPhoneNumber();
        securityService.validateOTP(sourcePhone, dto.otp());
        Transaction processed = transactionService.processTransaction(transaction);
        return new ResponseEntity<>(TransactionResultDto
                                            .builder()
                                            .transaction(processed)
                                            .message(messageSource.getMessage("transaction.transfer.processed",
                                                                              null,
                                                                              LocaleContextHolder.getLocale()))
                                            .build(), HttpStatus.OK);
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
        transactionService.initiateTransaction(dto.transactionDetails());
        String message = messageSource.getMessage("transaction.schedule.success",
                                                  new String[]{scheduleResult.transactionId().toString(), scheduleResult.timestamp().toString(), scheduleResult.scheduleId().toString()},
                                                  LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new TransactionScheduleResponseDto(message, scheduleResult.scheduleId()),
                                    HttpStatus.CREATED);
    }

}
