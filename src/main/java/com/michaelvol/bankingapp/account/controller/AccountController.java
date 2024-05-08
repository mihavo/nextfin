package com.michaelvol.bankingapp.account.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.dto.CreateAccountResponseDto;
import com.michaelvol.bankingapp.account.dto.DepositAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.DepositAmountResponseDto;
import com.michaelvol.bankingapp.account.dto.GetAccountBalanceDto;
import com.michaelvol.bankingapp.account.dto.GetAccountResponseDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountResponseDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.AccountService;
import com.michaelvol.bankingapp.transaction.dto.GetTransactionOptions;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/accounts")
@Tag(name = "Accounts API", description = "Methods for account operations")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final MessageSource messageSource;

    @PostMapping("")
    @Operation(summary = "Account initialization", description = "Method for creating accounts for non-employees i.e. holders")
    public ResponseEntity<CreateAccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto dto) {
        Account account = accountService.createAccount(dto);
        CreateAccountResponseDto responseDto = new CreateAccountResponseDto(account.getId(),
                                                                            "Your account has been created successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets account data", description = "Method for fetching account information")
    public ResponseEntity<GetAccountResponseDto> getAccountData(@PathVariable(name = "id") @NotNull @Positive Long accountId) {
        Account account = accountService.getAccount(accountId);
        GetAccountResponseDto responseDto = new GetAccountResponseDto(account, "Fetched account successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "Deposit to account", description = "Method for depositing a specified amount to an account")
    public ResponseEntity<DepositAmountResponseDto> depositAmount(@PathVariable @NotNull Long id, @Valid @RequestBody DepositAmountRequestDto dto) {
        BigDecimal updatedBalance = accountService.depositAmount(id, dto);
        return new ResponseEntity<>(new DepositAmountResponseDto(updatedBalance,
                                                                 messageSource.getMessage("account.deposit.success",
                                                                                          null,
                                                                                          LocaleContextHolder.getLocale())),
                                    HttpStatus.OK);
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw from account", description = "Method for withdrawing a specified amount from an account")
    public ResponseEntity<WithdrawAmountResponseDto> withdrawAmount(@PathVariable @NotNull Long id, @Valid @RequestBody WithdrawAmountRequestDto dto) {
        BigDecimal updatedBalance = accountService.withdrawAmount(id, dto);
        return new ResponseEntity<>(new WithdrawAmountResponseDto(updatedBalance,
                                                                  messageSource.getMessage(
                                                                          "account.withdraw.success",
                                                                          null,
                                                                          LocaleContextHolder.getLocale())),
                                    HttpStatus.OK);
    }

    @GetMapping("/{id}/balance")
    @Operation(summary = "Gets account balance", description = "Method for fetching current account balance")
    public ResponseEntity<String> checkBalance(@PathVariable @NotNull Long id) {
        GetAccountBalanceDto dto = accountService.checkBalance(id);
        return new ResponseEntity<>(messageSource.getMessage("account.balance.check",
                                                             new String[]{dto.balance().toString(), dto.currency().getSymbol()},
                                                             LocaleContextHolder.getLocale()), HttpStatus.OK);
    }

    @GetMapping("/{id}/transactions")
    @Operation(summary = "Gets recent transactions of the account")
    public ResponseEntity<Page<Transaction>> getTransactions(@PathVariable @NotNull Long id, @Valid GetTransactionOptions options) {
        Account account = accountService.getAccount(id);
        Page<Transaction> filteredTransactions = transactionService.getAccountTransactions(account, options);
        return new ResponseEntity<>(filteredTransactions, HttpStatus.OK);
    }
}
